package com.young.domain.cart.dto.response

import com.young.domain.option.domain.entity.ItemOptionValue
import com.young.domain.option.domain.enums.ItemOptionType

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