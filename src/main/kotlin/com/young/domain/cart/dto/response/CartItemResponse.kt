package com.young.domain.cart.dto.response

import com.young.domain.cart.domain.entity.CartItem

data class CartItemResponse(
    val id: Long,
    val item: CartItemItemResponse,
    val amount: Long
) {
    companion object {
        fun of(cartItem: CartItem): CartItemResponse {
            return CartItemResponse(
                id = cartItem.id!!,
                item = CartItemItemResponse.of(cartItem.item),
                amount = cartItem.amount
            )
        }
    }
}