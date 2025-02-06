package com.young.domain.item.dto.response

import com.young.domain.option.domain.entity.ItemOptionValue

data class ItemOptionResponse (
    val id: Long,
    val optionValues: List<ItemOptionValueResponse>
) {
    companion object {
        fun of(itemOptionValues: List<ItemOptionValue>) : ItemOptionResponse {
            return ItemOptionResponse(
                id = itemOptionValues.firstOrNull()?.itemOption?.id ?: 0,
                optionValues = itemOptionValues.map{ ItemOptionValueResponse.of(it) }
            )
        }
    }
}