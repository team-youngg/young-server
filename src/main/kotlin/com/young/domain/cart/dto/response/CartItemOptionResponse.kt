package com.young.domain.cart.dto.response

import com.young.domain.cart.domain.entity.CartItemOption
import com.young.domain.item.domain.entity.ItemOptionValue

data class CartItemOptionResponse(
    val itemOptionId: Long,
    val optionValues: List<CartItemOptionValueResponse>,
    val count: Long
) {
    companion object {
        fun of(itemOptionValues: List<ItemOptionValue>, cartItemOption: CartItemOption): CartItemOptionResponse {
            return CartItemOptionResponse(
                itemOptionId = itemOptionValues.firstOrNull()?.itemOption?.id!!,
                optionValues = itemOptionValues.map { CartItemOptionValueResponse.of(it) },
                count = cartItemOption.count
            )
        }
    }
}