package com.young.domain.payment.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.young.domain.item.repository.ItemOptionRepository
import com.young.domain.order.domain.enums.OrderStatus
import com.young.domain.order.error.OrderError
import com.young.domain.order.repository.OrderItemOptionRepository
import com.young.domain.order.repository.OrderItemRepository
import com.young.domain.order.repository.OrderRepository
import com.young.domain.payment.config.paymentProperties
import com.young.domain.payment.domain.entity.Payment
import com.young.domain.payment.dto.request.PayRequest
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
    private val orderItemRepository: OrderItemRepository
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
        updateOrderStatus(request.orderId)

        return paymentResponse
    }

    @Transactional
    fun updateOrderStatus(orderId: UUID) {
        val order = orderRepository.findByIdOrNull(orderId)

        if (order != null) {
            order.status = OrderStatus.PAID
            orderRepository.save(order)

            val orderItems = orderItemRepository.findByOrder(order)
            for (orderItem in orderItems) {
                val orderItemOptions = orderItemOptionRepository.findByOrderItem(orderItem)
                for (orderOption in orderItemOptions) {
                    orderOption.itemOption.stock -= orderOption.count
                    itemOptionRepository.save(orderOption.itemOption)
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
}