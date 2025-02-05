package com.young.domain.order.repository

import com.young.domain.order.domain.entity.Order
import com.young.domain.payment.domain.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrderRepository : JpaRepository<Order, UUID> {
    fun existsByPayment(payment: Payment): Boolean
}