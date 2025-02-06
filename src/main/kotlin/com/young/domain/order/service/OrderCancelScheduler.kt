package com.young.domain.order.service

import com.young.domain.order.domain.enums.OrderStatus
import com.young.domain.order.repository.OrderRepository
import com.young.domain.payment.service.PaymentService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class OrderCancelScheduler(
    private val orderRepository: OrderRepository,
    private val paymentService: PaymentService
) {
    @Scheduled(fixedRate = 5 * 60 * 1000)
    @Transactional
    fun cancelUnpaidOrders() {
        val twentyMinutesAgo = LocalDateTime.now().minusMinutes(20)

        val unpaidOrders = orderRepository.findByStatusAndCreatedAtBefore(OrderStatus.PENDING, twentyMinutesAgo)

        for (order in unpaidOrders) {
            paymentService.cancelOrder(order.id!!)
            println("⏳ 미결제 주문 자동 취소: ${order.id}")
        }
    }
}
