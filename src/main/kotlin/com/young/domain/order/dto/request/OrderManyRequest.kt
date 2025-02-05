package com.young.domain.order.dto.request

data class OrderManyRequest(
    val items: List<OrderRequest>,
    val paymentKey: String
)