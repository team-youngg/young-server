package com.young.domain.order.repository

import com.young.domain.order.domain.entity.OrderItem
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository : JpaRepository<OrderItem, Long> {
}