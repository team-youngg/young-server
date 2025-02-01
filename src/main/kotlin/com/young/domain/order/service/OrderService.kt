package com.young.domain.order.service

import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.ItemRepository
import com.young.domain.order.domain.entity.Order
import com.young.domain.order.domain.entity.OrderItem
import com.young.domain.order.domain.enums.OrderStatus
import com.young.domain.order.dto.request.OrderFromCartRequest
import com.young.domain.order.dto.request.OrderOneRequest
import com.young.domain.order.repository.OrderItemRepository
import com.young.domain.order.repository.OrderRepository
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService (
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val securityHolder: SecurityHolder,
    private val itemRepository: ItemRepository
) {
    @Transactional
    fun orderOne(request: OrderOneRequest) {
        order(listOf(request))
    }

    @Transactional
    fun orderFromCart(request: OrderFromCartRequest) {
        order(request.items)
    }

    @Transactional
    fun order(requests: List<OrderOneRequest>) {
        val user = securityHolder.user

        val order = Order(
            user = user,
            status = OrderStatus.PAID
        )
        orderRepository.save(order)

        val orderItems = requests.map { request ->
            val item = itemRepository.findByIdOrNull(request.itemId)
                ?: throw CustomException(ItemError.ITEM_NOT_FOUND)

            OrderItem(
                order = order,
                count = request.count,
                option = request.option,
                item = item
            )
        }

        orderItemRepository.saveAll(orderItems)
    }
}