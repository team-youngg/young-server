package com.young.domain.order.dto.request

data class UpdateOrderOptionRequest(
    val orderItemOptionId: Long,
    val newItemOptionId: Long
)