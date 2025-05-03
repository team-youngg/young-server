package com.young.domain.payment.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.young.domain.cart.error.CartError
import com.young.domain.cart.repository.CartItemOptionRepository
import com.young.domain.item.error.ItemError
import com.young.domain.option.repository.ItemOptionRepository
import com.young.domain.order.domain.enums.OrderStatus
import com.young.domain.order.error.OrderError
import com.young.domain.order.repository.OrderItemOptionRepository
import com.young.domain.order.repository.OrderItemRepository
import com.young.domain.order.repository.OrderRepository
import com.young.domain.payment.config.paymentProperties
import com.young.domain.payment.domain.entity.Payment
import com.young.domain.payment.dto.request.PayRequest
import com.young.domain.payment.dto.request.PaymentCancelRequest
import com.young.domain.payment.dto.response.PaymentCancelResponse
import com.young.domain.payment.dto.response.PaymentHistoryResponse
import com.young.domain.payment.dto.response.PaymentResponse
import com.young.domain.payment.error.PaymentError
import com.young.domain.payment.repository.PaymentRepository
import com.young.domain.user.error.UserError
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.time.OffsetDateTime
import java.util.*

@Service
class PaymentService (
    private val paymentProperties: paymentProperties,
    private val securityHolder: SecurityHolder,
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val itemOptionRepository: ItemOptionRepository,
    private val orderItemOptionRepository: OrderItemOptionRepository,
    private val orderItemRepository: OrderItemRepository,
    private val cartItemOptionRepository: CartItemOptionRepository,
) {
    @Transactional
    fun getHeader(): String {
        val authorizations = "Basic " + Base64.getEncoder()
            .encodeToString("${paymentProperties.apiSecret}:".toByteArray(StandardCharsets.UTF_8))
        return authorizations
    }

    @Transactional
    fun confirmPayment(request: PayRequest) : PaymentResponse {
        val order = orderRepository.findByIdOrNull(request.orderId)
            ?: throw CustomException(OrderError.ORDER_NOT_FOUND)

        if (order.status == OrderStatus.PAID) {
            throw CustomException(PaymentError.ALREADY_PAID)
        }

        val webClient = WebClient.builder()
            .baseUrl("https://api.tosspayments.com/v1")
            .defaultHeader("Authorization", getHeader())
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .build()

        val objectMapper = jacksonObjectMapper()

        val response: String = webClient.post()
            .uri("/payments/confirm")
            .bodyValue(request)
            .retrieve()
            .onStatus({ it.isError }) { response ->
                response.bodyToMono(String::class.java).flatMap {
                    Mono.error(CustomException(PaymentError.API_ERROR, it.toString()))
                }
            }
            .bodyToMono(String::class.java)
            .block() ?: throw CustomException(PaymentError.API_ERROR, "No response")

        println("🔎 Toss API 응답 데이터: $response")

        val paymentResponse = objectMapper.readValue(response, PaymentResponse::class.java)

        if (paymentResponse.status != "DONE") {
            cancelOrder(request.orderId)
            throw CustomException(PaymentError.PAYMENT_FAILED, paymentResponse.failure?.message.toString())
        }

        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)

        val payment = Payment(
            orderId = paymentResponse.orderId,
            paymentKey = paymentResponse.paymentKey,
            totalPrice = paymentResponse.totalAmount,
            payMethod = paymentResponse.method ?: "UNKNOWN",
            paidAt = OffsetDateTime.parse(paymentResponse.approvedAt!!).toLocalDateTime(),
            bankCode = paymentResponse.easyPay?.provider ?: "N/A",
            bankName = paymentResponse.easyPay?.provider ?: "N/A",
            isPaid = paymentResponse?.status == "DONE",
            user = user
        )

        paymentRepository.save(payment)
        updateOrderStatus(request.orderId, payment)

        return paymentResponse
    }

    @Transactional
    fun updateOrderStatus(orderId: UUID, payment: Payment) {
        val order = orderRepository.findByIdOrNull(orderId)

        if (order != null) {
            order.payment = payment
            order.status = OrderStatus.PAID
            orderRepository.save(order)

            val orderItems = orderItemRepository.findByOrder(order)
            for (orderItem in orderItems) {
                val orderItemOptions = orderItemOptionRepository.findByOrderItem(orderItem)
                for (orderOption in orderItemOptions) {
                    orderOption.itemOption.stock -= orderOption.count
                    itemOptionRepository.save(orderOption.itemOption)

                    // 주문 완료 후 장바구니에서 상품 삭제
                    val cartItemOption = cartItemOptionRepository.findByItemOption(orderOption.itemOption)
                        ?: throw CustomException(ItemError.ITEM_NOT_FOUND)
                    cartItemOptionRepository.delete(cartItemOption)
                }
            }
        }
    }

    @Transactional
    fun cancelOrder(orderId: UUID) {
        val order = orderRepository.findByIdOrNull(orderId) ?: return
        val orderItems = orderItemRepository.findByOrder(order)

        for (orderItem in orderItems) {
            val orderItemOptions = orderItemOptionRepository.findByOrderItem(orderItem)
            orderItemOptionRepository.deleteAll(orderItemOptions)
        }
        orderItemRepository.deleteAll(orderItems)

        order.status = OrderStatus.CANCELED
        orderRepository.save(order)
    }

    @Transactional
    fun cancelPayment(request: PaymentCancelRequest): PaymentCancelResponse {
        val order = orderRepository.findByIdOrNull(request.orderId)
            ?: throw CustomException(OrderError.ORDER_NOT_FOUND)

        if (order.status == OrderStatus.CHECKED ||
            order.status == OrderStatus.SHIPPING ||
            order.status == OrderStatus.COMPLETED) {
            throw CustomException(PaymentError.ALREADY_CHECKED)
        }
        if (order.status != OrderStatus.PAID) {
            throw CustomException(PaymentError.PAYMENT_NOT_PAID)
        }

        val cancelItemOption = orderItemOptionRepository.findByIdOrNull(request.itemOptionId)
            ?: throw CustomException(ItemError.OPTION_NOT_FOUND)
        val cancelAmount = cancelItemOption.count * cancelItemOption.orderItem.price

        val webClient = WebClient.builder()
            .baseUrl("https://api.tosspayments.com/v1")
            .defaultHeader("Authorization", getHeader())
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .build()

        val objectMapper = jacksonObjectMapper()

        val requestBody = mapOf("cancelReason" to request.cancelReason, "cancelAmount" to cancelAmount)

        val url = "/payments/${request.paymentKey}/cancel"
        val response: String = webClient.post()
            .uri(url)
            .bodyValue(requestBody)
            .retrieve()
            .onStatus({ it.isError }) { res ->
                res.bodyToMono(String::class.java).flatMap {
                    Mono.error(CustomException(PaymentError.API_ERROR, it))
                }
            }
            .bodyToMono(String::class.java)
            .block() ?: throw CustomException(PaymentError.API_ERROR, "No response")

        println("🔎 Toss API 취소 응답 데이터: $response")

        val cancelResponse = objectMapper.readValue(response, PaymentCancelResponse::class.java)

        if (cancelResponse.status != "CANCELED") {
            throw CustomException(
                PaymentError.PAYMENT_CANCELLATION_FAILED,
                cancelResponse.failure?.message ?: "취소 실패"
            )
        }

        // 주문 상태 업데이트 (취소)
        order.status = OrderStatus.CANCELED
        orderRepository.save(order)

        return cancelResponse
    }

    @Transactional(readOnly = true)
    fun getPaymentByOrderId(orderId: UUID): PaymentHistoryResponse {
        val payment = paymentRepository.findByOrderId(orderId.toString())
            ?: throw CustomException(OrderError.ORDER_NOT_FOUND)
        return PaymentHistoryResponse.of(payment)
    }
}