package com.young.domain.order.repository

import com.young.domain.order.domain.entity.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {
}