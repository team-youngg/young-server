package com.young.domain.order.service

import com.young.domain.info.error.InfoError
import com.young.domain.item.error.ItemError
import com.young.domain.option.repository.ItemOptionRepository
import com.young.domain.item.repository.ItemRepository
import com.young.domain.item.util.ItemUtil
import com.young.domain.order.domain.entity.Order
import com.young.domain.order.domain.entity.OrderItem
import com.young.domain.order.domain.entity.OrderItemOption
import com.young.domain.order.domain.enums.OrderStatus
import com.young.domain.order.dto.request.OrderManyRequest
import com.young.domain.order.dto.request.OrderRequest
import com.young.domain.order.dto.request.UpdateOrderOptionRequest
import com.young.domain.order.dto.response.OrderInfoResponse
import com.young.domain.order.dto.response.OrderItemResponse
import com.young.domain.order.dto.response.OrderResponse
import com.young.domain.order.repository.OrderItemOptionRepository
import com.young.domain.order.repository.OrderItemRepository
import com.young.domain.order.repository.OrderRepository
import com.young.domain.user.error.UserError
import com.young.domain.info.repository.UserOrderInfoRepository
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.data.domain.Pageable
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
    private val userOrderInfoRepository: UserOrderInfoRepository,
    private val itemUtil: ItemUtil
) {
    @Transactional
    fun order(requests: OrderManyRequest): OrderInfoResponse {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val orderInfo = userOrderInfoRepository.findByIdOrNull(requests.orderInfoId)
            ?: throw CustomException(InfoError.INFO_NOT_FOUND)

        if (orderInfo.user.id != user.id) throw CustomException(InfoError.INFO_NOT_MATCH)

        val order = Order(
            user = user,
            status = OrderStatus.PENDING,
            orderInfo = orderInfo
        )
        orderRepository.save(order)

        val amount = requests.items.sumOf { orderProcess(it, order) }
        order.amount = amount
        orderRepository.save(order)

        return OrderInfoResponse(order.id!!, amount)
    }

    @Transactional
    fun orderProcess(request: OrderRequest, order: Order): Long {
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

        return item.price * request.count
    }

    @Transactional
    fun updateOrderOption(request: UpdateOrderOptionRequest) {
        val orderItemOption = orderItemOptionRepository.findByIdOrNull(request.orderItemOptionId)
            ?: throw CustomException(ItemError.OPTION_NOT_FOUND)
        val newItemOption = itemOptionRepository.findByIdOrNull(request.newItemOptionId)
            ?: throw CustomException(ItemError.ITEM_NOT_FOUND)

        if (newItemOption.item.id != orderItemOption.orderItem.item.id)
            throw CustomException(ItemError.ITEM_OPTION_NOT_MATCH)

        orderItemOption.itemOption = newItemOption
        orderItemOptionRepository.save(orderItemOption)
    }

    @Transactional
    fun getOrderLogs(pageable: Pageable): List<OrderResponse> {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val orders = orderRepository.findAllByUser(user, pageable).toList()

        return orders.map { order ->
            val orderItems = orderItemRepository.findByOrder(order)
            val orderItemResponses = orderItems.flatMap { orderItem ->
                val options = orderItemOptionRepository.findByOrderItem(orderItem)

                options.map { option ->
                    val itemElements = itemUtil.getItemElements(orderItem.item)

                    OrderItemResponse.of(
                        orderItem,
                        itemElements.images,
                        option,
                        itemElements.optionValues.filter { it.itemOption.id == option.itemOption.id }
                    )
                }
            }
            OrderResponse.of(order, orderItemResponses, order.amount!!)
        }
    }
}