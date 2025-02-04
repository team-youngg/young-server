package com.young.domain.item.dto.response

import com.young.domain.item.domain.entity.*

data class ItemResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Long,
    val options: List<OptionResponse>,
    val images: List<String>,
    val stock: Long,
    val categories: List<String>,
    val isWish: Boolean,
) {
    companion object {
        fun of(item: Item, images: List<String>, options: List<ItemOption>,
               optionValues: List<ItemOptionValue>, categories: List<Category>,
               isWish: Boolean): ItemResponse {
            return ItemResponse(
                id = item.id!!,
                name = item.name,
                description = item.description,
                price = item.price,
                options = options.map { itemOption ->
                    OptionResponse.of(
                        optionValues.filter { it.itemOption.id == itemOption.id }
                    )
                },
                images = images,
                stock = options.sumOf { it.stock },
                categories = categories.map { it.name },
                isWish = isWish,
            )
        }
    }
}