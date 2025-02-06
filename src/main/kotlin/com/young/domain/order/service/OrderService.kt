package com.young.domain.order.service

import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.ItemOptionRepository
import com.young.domain.item.repository.ItemRepository
import com.young.domain.order.domain.entity.Order
import com.young.domain.order.domain.entity.OrderItem
import com.young.domain.order.domain.entity.OrderItemOption
import com.young.domain.order.domain.enums.OrderStatus
import com.young.domain.order.dto.request.OrderManyRequest
import com.young.domain.order.dto.request.OrderRequest
import com.young.domain.order.dto.request.UpdateOrderOptionRequest
import com.young.domain.order.dto.response.OrderIdResponse
import com.young.domain.order.error.OrderError
import com.young.domain.order.repository.OrderItemOptionRepository
import com.young.domain.order.repository.OrderItemRepository
import com.young.domain.order.repository.OrderRepository
import com.young.domain.user.error.UserError
import com.young.domain.user.repository.UserOrderInfoRepository
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
    private val orderItemOptionRepository: OrderItemOptionRepository,
    private val itemOptionRepository: ItemOptionRepository,
    private val userOrderInfoRepository: UserOrderInfoRepository
) {
    @Transactional
    fun order(requests: OrderManyRequest): OrderIdResponse {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val orderInfo = userOrderInfoRepository.findByIdOrNull(requests.orderInfoId)
            ?: throw CustomException(UserError.INFO_NOT_FOUND)

        if (orderInfo.user.id == user.id) throw CustomException(UserError.INFO_NOT_FOUND)

        val order = Order(
            user = user,
            status = OrderStatus.PENDING,
            orderInfo = orderInfo
        )
        orderRepository.save(order)

        for (request in requests.items) {
            orderProcess(request, order)
        }

        return OrderIdResponse(order.id!!)
    }

    @Transactional
    fun orderProcess(request: OrderRequest, order: Order) {
        val itemOption = itemOptionRepository.findByIdOrNull(request.itemOptionId)
            ?: throw CustomException(ItemError.OPTION_NOT_FOUND)
        val item = itemRepository.findByIdOrNull(itemOption.item.id)
            ?: throw CustomException(ItemError.ITEM_NOT_FOUND)

        if (itemOption.stock < request.count) {
            throw CustomException(ItemError.NO_STOCK)
        }

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

    @Transactional
    fun updateOrderOption(request: UpdateOrderOptionRequest) {
        val orderItemOption = orderItemOptionRepository.findByIdOrNull(request.orderItemOptionId)
            ?: throw CustomException(ItemError.OPTION_NOT_FOUND)
        val newItemOption = itemOptionRepository.findByIdOrNull(request.newItemOptionId)
            ?: throw CustomException(ItemError.ITEM_NOT_FOUND)

        if (newItemOption.item.id != orderItemOption.orderItem.item.id) throw CustomException(ItemError.ITEM_OPTION_NOT_MATCH)

        orderItemOption.itemOption = newItemOption
        orderItemOptionRepository.save(orderItemOption)
    }
}