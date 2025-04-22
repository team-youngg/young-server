package com.young.domain.order.dto.request

import com.young.domain.order.domain.enums.OrderStatus

data class UpdateOrderRequest(
    val orderStatus: OrderStatus,
)
