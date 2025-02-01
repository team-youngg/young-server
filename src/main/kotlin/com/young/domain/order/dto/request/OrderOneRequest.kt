package com.young.domain.order.dto.request

data class OrderOneRequest(
    val itemId: Long,
    val count: Long,
    val option: String?
)