package com.young.domain.order.dto.request

data class CreateOrderOneRequest(
    val itemId: Long,
    val amount: Long,
    val option: String?
)
