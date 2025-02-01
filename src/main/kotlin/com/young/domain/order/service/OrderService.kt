package com.young.domain.order.service

import com.young.domain.order.repository.OrderItemRepository
import com.young.domain.order.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService (
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository
) {
    @Transactional
    fun createOrderOne() {}

    @Transactional
    fun createOrderFromCart() {}

    @Transactional
    fun order() {}
}