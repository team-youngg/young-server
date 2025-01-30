package com.young.domain.cart.dto.request

data class UpdateCartRequest(
    val cartItemId: Long,
    val amount: Long?
)