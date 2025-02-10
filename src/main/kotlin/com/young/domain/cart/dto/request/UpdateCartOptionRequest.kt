package com.young.domain.cart.dto.request

data class UpdateCartOptionRequest (
    val cartItemOptionId: Long,
    val newItemOptionId: Long
)