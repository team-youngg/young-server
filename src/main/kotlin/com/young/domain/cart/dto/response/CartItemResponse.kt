package com.young.domain.cart.dto.response

import com.young.domain.cart.domain.entity.CartItemOption
import com.young.domain.item.domain.entity.ItemOptionValue
import com.young.domain.item.dto.response.ItemResponse

data class CartItemResponse(
    val id: Long,
    val item: ItemResponse,
    val options: CartItemOptionResponse
) {
    companion object {
        fun of(itemOption: CartItemOption, itemOptionValues: List<ItemOptionValue>, item: ItemResponse): CartItemResponse {
            return CartItemResponse(
                id = itemOption.cartItem.id!!,
                item = item,
                options = CartItemOptionResponse.of(itemOptionValues, itemOption)
            )
        }
    }
}