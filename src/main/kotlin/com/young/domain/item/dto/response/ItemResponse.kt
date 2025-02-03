package com.young.domain.item.dto.response

import com.young.domain.item.domain.entity.Item
import com.young.domain.item.domain.entity.ItemOption

data class ItemResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Long,
    val options: List<OptionResponse>,
    val images: List<String>,
    val stock: Long
) {
    companion object {
        fun of(item: Item, images: List<String>, options: List<ItemOption>): ItemResponse {
            return ItemResponse(
                id = item.id!!,
                name = item.name,
                description = item.description,
                price = item.price,
                options = options.map { OptionResponse.of(it) },
                images = images,
                stock = options.sumOf { it.stock }
            )
        }
    }
}