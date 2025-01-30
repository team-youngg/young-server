package com.young.domain.cart.dto.request

data class UpdateCartRequest(
    val itemId: Long,
    val amount: Long?
)