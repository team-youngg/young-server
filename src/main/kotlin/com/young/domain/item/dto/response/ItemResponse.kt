package com.young.domain.item.dto.response

import com.young.domain.item.domain.entity.Item

data class ItemResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int,
    val images: List<String>
) {
    companion object {
        fun of(item: Item, images: List<String>): ItemResponse {
            return ItemResponse(
                id = item.id!!,
                name = item.name,
                description = item.description,
                price = item.price,
                stock = item.stock,
                images = images
            )
        }
    }
}