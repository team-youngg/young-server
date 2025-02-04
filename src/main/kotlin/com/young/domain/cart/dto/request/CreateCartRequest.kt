package com.young.domain.cart.dto.request

data class CreateCartRequest(
    val itemId: Long,
    val optionId: Long
)