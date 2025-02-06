package com.young.domain.order.repository

import com.young.domain.order.domain.entity.Order
import com.young.domain.order.domain.enums.OrderStatus
import com.young.domain.payment.domain.entity.Payment
import com.young.domain.user.domain.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.UUID

interface OrderRepository : JpaRepository<Order, UUID> {
    fun existsByPayment(payment: Payment): Boolean
    fun findByStatusAndCreatedAtBefore(status: OrderStatus, createdAt: LocalDateTime): List<Order>
    fun findAllByUser(user: User, pageable: Pageable): Page<Order>
}