package com.young.domain.cart.dto.response

import com.young.domain.cart.domain.entity.CartItemOption
import com.young.domain.item.domain.entity.ItemOptionValue
import com.young.domain.item.dto.response.ItemOptionValueResponse

data class CartItemResponse(
    val cartItemId: Long,
    val itemId: Long,
    val itemOptionValues: List<ItemOptionValueResponse> // TODO id, count 추가
) {
    companion object {
        fun of(itemOption: CartItemOption, itemOptionValues: List<ItemOptionValue>): CartItemResponse {
            return CartItemResponse(
                cartItemId = itemOption.cartItem.id!!,
                itemId = itemOption.cartItem.item.id!!,
                itemOptionValues = itemOptionValues.map { ItemOptionValueResponse.of(it) }
            )
        }
    }
}
