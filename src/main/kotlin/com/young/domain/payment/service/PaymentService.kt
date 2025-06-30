package com.young.domain.payment.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.young.domain.cart.repository.CartItemOptionRepository
import com.young.domain.item.error.ItemError
import com.young.domain.option.repository.ItemOptionRepository
import com.young.domain.order.domain.entity.Order
import com.young.domain.order.domain.enums.OrderStatus
import com.young.domain.order.error.OrderError
import com.young.domain.order.repository.OrderItemOptionRepository
import com.young.domain.order.repository.OrderItemRepository
import com.young.domain.order.repository.OrderRepository
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
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*

@Service
class PaymentService (
    private val securityHolder: SecurityHolder,
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val itemOptionRepository: ItemOptionRepository,
    private val orderItemOptionRepository: OrderItemOptionRepository,
    private val orderItemRepository: OrderItemRepository,
    private val cartItemOptionRepository: CartItemOptionRepository,
    private val tossClient: WebClient
) {
    @Transactional
    fun confirmPayment(request: PayRequest) : PaymentResponse {
        val order = orderRepository.findByIdOrNull(request.orderId)
            ?: throw CustomException(OrderError.ORDER_NOT_FOUND)

        if (order.status == OrderStatus.PAID) {
            throw CustomException(PaymentError.ALREADY_PAID)
        }

        val objectMapper = jacksonObjectMapper()

        val response: String = tossClient.post()
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

        val paymentResponse = objectMapper.readValue(response, PaymentResponse::class.java)

        if (paymentResponse.status != "DONE") {
            cancelOrder(request.orderId)
            throw CustomException(PaymentError.PAYMENT_FAILED, paymentResponse.failure?.message.toString())
        }

        return savePayment(paymentResponse, order)
    }

    @Transactional
    fun savePayment(response: PaymentResponse, order: Order): PaymentResponse {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)

        val payment = Payment(
            orderId = response.orderId,
            paymentKey = response.paymentKey,
            totalPrice = response.totalAmount,
            payMethod = response.method ?: "UNKNOWN",
            paidAt = response.approvedAt
                ?.let { OffsetDateTime.parse(it).toLocalDateTime() }
                ?: LocalDateTime.now(),
            bankCode = response.easyPay?.provider ?: "N/A",
            bankName = response.easyPay?.provider ?: "N/A",
            isPaid = true,
            user = user
        )
        paymentRepository.save(payment)

        order.apply {
            status = OrderStatus.PAID
            this.payment = payment
        }
        orderRepository.save(order)

        orderItemRepository.findByOrder(order).forEach { orderItem ->
            orderItemOptionRepository.findByOrderItem(orderItem).forEach { orderItemOption ->
                val updatedRows = itemOptionRepository.decreaseStock(
                    orderItemOption.itemOption.id!!, orderItemOption.count
                )
                if (updatedRows == 0) throw CustomException(ItemError.NO_STOCK)

                val cartItemOption = cartItemOptionRepository
                    .findByItemOptionAndCartItem_Cart_User(orderItemOption.itemOption, user)
                cartItemOption?.let { cartItemOptionRepository.delete(it) }
            }
        }

        return response
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

    @Transactional(readOnly = true)
    fun getPaymentByOrderId(orderId: UUID): PaymentHistoryResponse {
        val payment = paymentRepository.findByOrderId(orderId.toString())
            ?: throw CustomException(OrderError.ORDER_NOT_FOUND)
        return PaymentHistoryResponse.of(payment)
    }
}