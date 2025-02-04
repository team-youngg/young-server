package com.young.domain.cart.dto.response

import com.young.domain.item.domain.entity.ItemOptionValue
import com.young.domain.item.domain.enums.ItemOptionType

data class CartItemOptionValueResponse(
    val type: ItemOptionType,
    val value: String
) {
    companion object {
        fun of(itemOptionValue: ItemOptionValue): CartItemOptionValueResponse {
            return CartItemOptionValueResponse(
                type = itemOptionValue.type,
                value = itemOptionValue.value,
            )
        }
    }
}