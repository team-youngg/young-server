package com.young.domain.item.dto.response

import com.young.domain.category.domain.entity.Category
import com.young.domain.item.domain.entity.Item
import com.young.domain.option.domain.entity.ItemOption
import com.young.domain.option.domain.entity.ItemOptionValue

data class ItemDetailResponse(
    val id: Long,
    val name: String,
    val description: String,
    val detail: String,
    val price: Long,
    val options: List<ItemOptionStockResponse>,
    val images: List<String>,
    val categories: List<String>,
    val isWish: Boolean,
    val purchasable: Boolean,
) {
    companion object {
        fun of(item: Item, images: List<String>, options: List<ItemOption>,
               optionValues: List<ItemOptionValue>, categories: List<Category>,
               isWish: Boolean): ItemDetailResponse {
            return ItemDetailResponse(
                id = item.id!!,
                name = item.name,
                description = item.description,
                detail = item.detail,
                price = item.price,
                options = options.map { itemOption ->
                    ItemOptionStockResponse.of(optionValues
                        .filter { it.itemOption.id == itemOption.id })
                },
                images = images,
                categories = categories.map { it.name },
                isWish = isWish,
                purchasable = item.purchasable
            )
        }
    }
}