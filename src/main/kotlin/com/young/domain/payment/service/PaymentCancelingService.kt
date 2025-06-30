package com.young.domain.payment.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.young.domain.item.error.ItemError
import com.young.domain.order.domain.entity.Order
import com.young.domain.order.domain.entity.OrderItemOption
import com.young.domain.order.domain.enums.OrderStatus
import com.young.domain.order.error.OrderError
import com.young.domain.order.repository.OrderItemOptionRepository
import com.young.domain.order.repository.OrderRepository
import com.young.domain.payment.dto.request.PaymentCancelRequest
import com.young.domain.payment.dto.response.PaymentCancelResponse
import com.young.domain.payment.error.PaymentError
import com.young.global.exception.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.LocalDateTime

@Service
class PaymentCancelingService (
    private val orderRepository: OrderRepository,
    private val orderItemOptionRepository: OrderItemOptionRepository,
    private val tossClient: WebClient
) {
    fun cancelPayment(request: PaymentCancelRequest): PaymentCancelResponse {
        val context = buildCancelContext(request)
        val apiResult = fetchTossCancel(request, context.amount)
        applyCancelToDatabase(context)
        return apiResult
    }

    private fun buildCancelContext(request: PaymentCancelRequest): CancelContext {
        val order: Order = orderRepository.findByIdOrNull(request.orderId)
            ?: throw CustomException(OrderError.ORDER_NOT_FOUND)
        val payment = order.payment ?: throw CustomException(PaymentError.PAYMENT_NOT_FOUND)

        if (order.status !in listOf(OrderStatus.PAID, OrderStatus.PARTIAL_CANCELED))
            throw CustomException(PaymentError.PAYMENT_NOT_PAID)

        val hoursSincePaid = Duration.between(payment.paidAt, LocalDateTime.now()).toHours()
        if (hoursSincePaid > 360) {                       // 15*24 = 360
            throw CustomException(PaymentError.PAYMENT_EXPIRED)
        }

        // 부분 취소 대상 옵션 리스트 결정
        val targets: List<OrderItemOption> =
            if (request.itemOptionIds.isNullOrEmpty()) {
                orderItemOptionRepository.findByOrder(order)           // **조인쿼리 사용**
            } else {
                orderItemOptionRepository.findByIdsAndOrder(request.itemOptionIds, order).also {
                    if (it.size != request.itemOptionIds.size)
                        throw CustomException(ItemError.OPTION_NOT_FOUND)
                }
            }

        val amount = targets.sumOf { it.count * it.orderItem.price }
        return CancelContext(order, targets, amount)
    }

    private fun fetchTossCancel(request: PaymentCancelRequest, cancelAmount: Long): PaymentCancelResponse {
        val body = mapOf("cancelReason" to request.cancelReason,
            "cancelAmount" to cancelAmount)

        val rawJson: String = tossClient.post()
            .uri("/payments/${request.paymentKey}/cancel")
            .bodyValue(body)
            .retrieve()
            .onStatus({ it.isError }) { res ->
                res.bodyToMono(String::class.java)
                    .flatMap { Mono.error(CustomException(PaymentError.API_ERROR, it)) }
            }
            .bodyToMono(String::class.java)
            .block() ?: throw CustomException(PaymentError.API_ERROR, "No response")

        val response = jacksonObjectMapper().readValue(rawJson, PaymentCancelResponse::class.java)
        if (response.status !in listOf("CANCELED", "PARTIAL_CANCELED"))
            throw CustomException(PaymentError.PAYMENT_CANCELLATION_FAILED,
                response.failure?.message ?: "취소 실패")
        return response
    }

    @Transactional
    protected fun applyCancelToDatabase(context: CancelContext) {
        context.targets.forEach { orderItemOption ->
            // 재고 복구
//            itemOptionRepository.increaseStock(oio.itemOption.id!!, oio.count)
            // 옵션 레코드 삭제(부분 취소 시)
            orderItemOptionRepository.delete(orderItemOption)
        }

        // 남은 옵션이 있으면 PARTIAL_CANCELED, 없으면 CANCELED
        val remainAmount = orderItemOptionRepository.findByOrder(context.order).sumOf { it.count * it.orderItem.price }

        context.order.apply {
            status = if (remainAmount == 0L) OrderStatus.CANCELED
            else OrderStatus.PARTIAL_CANCELED
            amount = remainAmount
        }
        orderRepository.save(context.order)
    }

    data class CancelContext(
        val order: Order,
        val targets: List<OrderItemOption>,
        val amount: Long
    )
}