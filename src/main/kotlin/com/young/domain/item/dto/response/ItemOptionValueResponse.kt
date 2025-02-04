package com.young.domain.item.dto.response

import com.young.domain.item.domain.entity.ItemOptionValue
import com.young.domain.item.domain.enums.ItemOptionType

data class ItemOptionValueResponse(
    val type: ItemOptionType,
    val value: String,
    val detail: String?
) {
    companion object {
        fun of(itemOptionValue: ItemOptionValue): ItemOptionValueResponse {
            return ItemOptionValueResponse(
                type = itemOptionValue.type,
                value = itemOptionValue.value,
                detail = itemOptionValue.detail
            )
        }
    }
}