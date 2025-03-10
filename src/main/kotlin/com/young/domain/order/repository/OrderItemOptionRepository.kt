package com.young.domain.order.repository

import com.young.domain.order.domain.entity.OrderItem
import com.young.domain.order.domain.entity.OrderItemOption
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemOptionRepository : JpaRepository<OrderItemOption, Long> {
    fun findByOrderItem(orderItem: OrderItem): List<OrderItemOption>
}