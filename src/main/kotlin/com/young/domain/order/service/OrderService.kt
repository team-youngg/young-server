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
import com.young.domain.order.dto.response.OrderInfoResponse
import com.young.domain.order.dto.response.OrderItemResponse
import com.young.domain.order.dto.response.OrderResponse
import com.young.domain.order.repository.OrderItemOptionRepository
import com.young.domain.order.repository.OrderItemRepository
import com.young.domain.order.repository.OrderRepository
import com.young.domain.user.error.UserError
import com.young.domain.info.repository.UserOrderInfoRepository
import com.young.domain.order.dto.request.UpdateOrderRequest
import com.young.domain.order.dto.response.OrderPaymentResponse
import com.young.domain.order.error.OrderError
import com.young.domain.payment.service.PaymentService
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OrderService (
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,
    private val securityHolder: SecurityHolder,
    private val itemRepository: ItemRepository,
    private val orderItemOptionRepository: OrderItemOptionRepository,
    private val itemOptionRepository: ItemOptionRepository,
    private val userOrderInfoRepository: UserOrderInfoRepository,
    private val itemUtil: ItemUtil,
    private val paymentService: PaymentService,
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
            orderInfo = orderInfo,
            orderRequest = requests.orderRequest,
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
    fun getOrderLogs(pageable: Pageable): List<OrderResponse> {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val excludedStatuses = listOf(OrderStatus.PENDING, OrderStatus.CANCELED)
        val orders = orderRepository
            .findAllByUserAndStatusNotInOrderByCreatedAtDesc(user, excludedStatuses, pageable)
            .toList()

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

    @Transactional
    fun updateOrderStatus(orderId: UUID, request: UpdateOrderRequest) {
        val order = orderRepository.findByIdOrNull(orderId) ?: throw CustomException(OrderError.ORDER_NOT_FOUND)

        order.status = request.orderStatus
        orderRepository.save(order)
    }

    @Transactional(readOnly = true)
    fun getOrder(orderId: UUID): OrderPaymentResponse {
        val payment = paymentService.getPaymentByOrderId(orderId)

        return OrderPaymentResponse(payment = payment, order = getOrderById(orderId))
    }

    @Transactional(readOnly = true)
    fun getOrderById(orderId: UUID): OrderResponse {
        // 1) 인증된 유저 확인
        val user = securityHolder.user
            ?: throw CustomException(UserError.USER_NOT_FOUND)

        // 2) 주문 조회 및 본인 여부 체크
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw CustomException(OrderError.ORDER_NOT_FOUND)
        if (order.user.id != user.id) {
            throw CustomException(OrderError.ORDER_NOT_FOUND)
        }

        // 3) 주문 아이템 & 옵션 조합으로 OrderItemResponse 리스트 생성
        val orderItemResponses = orderItemRepository.findByOrder(order)
            .flatMap { orderItem ->
                orderItemOptionRepository.findByOrderItem(orderItem).map { opt ->
                    val elems = itemUtil.getItemElements(orderItem.item)
                    OrderItemResponse.of(
                        orderItem,
                        elems.images,
                        opt,
                        elems.optionValues.filter { it.itemOption.id == opt.itemOption.id }
                    )
                }
            }

        // 4) OrderResponse 생성
        return OrderResponse.of(order, orderItemResponses, order.amount!!)
    }
}