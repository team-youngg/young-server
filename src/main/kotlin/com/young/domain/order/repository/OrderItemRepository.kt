package com.young.domain.order.repository

import com.young.domain.item.domain.entity.Item
import com.young.domain.order.domain.entity.Order
import com.young.domain.order.domain.entity.OrderItem
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository : JpaRepository<OrderItem, Long> {
    fun findByOrder(order: Order): List<OrderItem>
    fun existsByItem(item: Item): Boolean
}