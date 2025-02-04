package com.young.domain.cart.dto.request

data class UpdateCartRequest(
    val isPlus: Boolean,
    val count: Long
)