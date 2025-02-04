package com.young.domain.order.service

import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.ItemOptionRepository
import com.young.domain.item.repository.ItemOptionValueRepository
import com.young.domain.item.repository.ItemRepository
import com.young.domain.order.domain.entity.Order
import com.young.domain.order.domain.entity.OrderItem
import com.young.domain.order.domain.entity.OrderItemOption
import com.young.domain.order.domain.enums.OrderStatus
import com.young.domain.order.dto.request.OrderManyRequest
import com.young.domain.order.dto.request.OrderRequest
import com.young.domain.order.repository.OrderItemOptionRepository
import com.young.domain.order.repository.OrderItemRepository
import com.young.domain.order.repository.OrderRepository
import com.young.domain.payment.repository.PaymentRepository
import com.young.domain.user.error.UserError
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
    private val itemRepository: ItemRepository,
    private val paymentRepository: PaymentRepository,
    private val orderItemOptionRepository: OrderItemOptionRepository,
    private val itemOptionValueRepository: ItemOptionValueRepository,
    private val itemOptionRepository: ItemOptionRepository,
) {
    @Transactional
    fun orderOne(request: OrderRequest) {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val order = Order(
            user = user,
            status = OrderStatus.ORDERED
        )
        orderRepository.save(order)
        order(request, order)
    }

    @Transactional
    fun orderFromCart(requests: OrderManyRequest) {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val order = Order(
            user = user,
            status = OrderStatus.ORDERED
        )
        orderRepository.save(order)
        for (item in requests.items) {
            order(item, order)
        }
    }

    @Transactional
    fun order(request: OrderRequest, order: Order) {
        // TODO payment 만들기

        // TODO 수량만큼 재고 삭감
        val itemOption = itemOptionRepository.findByIdOrNull(request.itemOptionId)
            ?: throw CustomException(ItemError.OPTION_NOT_FOUND)
        val item = itemRepository.findByIdOrNull(itemOption.item.id)
            ?: throw CustomException(ItemError.ITEM_NOT_FOUND)
        val orderItem = OrderItem(
            item = item,
            order = order,
            price = item.price,
        )
        orderItemRepository.save(orderItem)

        val orderItemOption = OrderItemOption(
            orderItem = orderItem,
            count = request.count,
            itemOption = itemOption
        )
        orderItemOptionRepository.save(orderItemOption)
    }
}