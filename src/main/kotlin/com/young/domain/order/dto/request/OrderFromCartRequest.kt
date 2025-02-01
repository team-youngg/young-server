package com.young.domain.order.dto.request

data class OrderFromCartRequest(
    val items: List<OrderOneRequest>
)