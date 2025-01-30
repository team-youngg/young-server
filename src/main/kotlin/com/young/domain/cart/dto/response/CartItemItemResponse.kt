package com.young.domain.cart.dto.response

import com.young.domain.item.domain.entity.Item

data class CartItemItemResponse(
    val name: String,
    val description: String,
    val price: Long,
) {
    companion object {
        fun of(item: Item): CartItemItemResponse {
            return CartItemItemResponse(
                name = item.name,
                description = item.description,
                price = item.price
            )
        }
    }
}