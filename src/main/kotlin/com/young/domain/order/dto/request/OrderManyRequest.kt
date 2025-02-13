package com.young.domain.order.dto.request

data class OrderManyRequest(
    val items: List<OrderRequest>,
    val orderInfoId: Long,
    val orderRequest: String
)