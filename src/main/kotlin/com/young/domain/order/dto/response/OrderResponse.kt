package com.young.domain.order.dto.response

import com.young.domain.order.domain.entity.Order
import java.time.LocalDateTime
import java.util.*

data class OrderResponse(
    val orderId: UUID,
    val date: LocalDateTime,
    val amount: Long,
    val items: List<OrderItemResponse>,
) {
    companion object {
        fun of(order: Order, items: List<OrderItemResponse>, amount: Long): OrderResponse {
            return OrderResponse(
                orderId = order.id!!,
                date = order.createdAt,
                amount = amount,
                items = items
            )
        }
    }
}
