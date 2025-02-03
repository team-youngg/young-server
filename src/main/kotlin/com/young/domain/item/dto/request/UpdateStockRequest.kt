package com.young.domain.item.dto.request

data class UpdateStockRequest(
    val isPlus: Boolean,
    val count: Long
)