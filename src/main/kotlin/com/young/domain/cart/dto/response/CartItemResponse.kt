package com.young.domain.cart.dto.response

import com.young.domain.cart.domain.entity.CartItemOption
import com.young.domain.item.domain.entity.ItemOptionValue

data class CartItemResponse(
    val id: Long,
    val itemId: Long,
    val options: CartItemOptionResponse
) {
    companion object {
        fun of(itemOption: CartItemOption, itemOptionValues: List<ItemOptionValue>): CartItemResponse {
            return CartItemResponse(
                id = itemOption.cartItem.id!!,
                itemId = itemOption.cartItem.item.id!!,
                options = CartItemOptionResponse.of(itemOptionValues, itemOption)
            )
        }
    }
}