package com.young.domain.item.dto.response

import com.young.domain.item.domain.entity.Category
import com.young.domain.item.domain.entity.Item
import com.young.domain.item.domain.entity.ItemOption
import com.young.domain.item.domain.entity.ItemOptionValue

data class ItemResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Long,
    val options: List<OptionResponse>,
    val images: List<String>,
    val stock: Long,
    val categories: List<CategoryResponse>,
) {
    companion object {
        fun of(item: Item, images: List<String>, options: List<ItemOption>, optionValues: List<ItemOptionValue>, categories: List<Category>): ItemResponse {
            return ItemResponse(
                id = item.id!!,
                name = item.name,
                description = item.description,
                price = item.price,
                options = options.map { itemOption ->
                    OptionResponse(
                        optionValues = optionValues
                            .filter { it.itemOption.id == itemOption.id }
                            .map { optionValue ->
                                ItemOptionValueResponse(
                                    type = optionValue.type,
                                    value = optionValue.value,
                                    detail = optionValue.detail
                                )
                            }
                    )
                },
                images = images,
                stock = options.sumOf { it.stock },
                categories = categories.map { CategoryResponse.of(it) },
            )
        }
    }
}