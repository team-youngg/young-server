package com.young.domain.item.dto.response

import com.young.domain.item.domain.entity.Category
import com.young.domain.item.domain.entity.Item
import com.young.domain.item.domain.entity.ItemOption
import com.young.domain.item.domain.entity.ItemOptionValue

data class ItemDetailResponse(
    val id: Long,
    val name: String,
    val description: String,
    val detail: String,
    val price: Long,
    val options: List<OptionStockResponse>,
    val images: List<String>,
    val categories: List<String>
) {
    companion object {
        fun of(item: Item, images: List<String>, options: List<ItemOption>, optionValues: List<ItemOptionValue>, categories: List<Category>): ItemDetailResponse {
            return ItemDetailResponse(
                id = item.id!!,
                name = item.name,
                description = item.description,
                detail = item.detail,
                price = item.price,
                options = options.map { itemOption ->
                    OptionStockResponse.of(optionValues
                        .filter { it.itemOption.id == itemOption.id })
                },
                images = images,
                categories = categories.map { it.name }
            )
        }
    }
}