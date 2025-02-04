package com.young.domain.item.dto.response

import com.young.domain.item.domain.entity.ItemOptionValue

data class OptionResponse (
    val id: Long,
    val optionValues: List<ItemOptionValueResponse>
) {
    companion object {
        fun of(itemOptionValues: List<ItemOptionValue>) : OptionResponse {
            return OptionResponse(
                id = itemOptionValues.firstOrNull()?.itemOption?.id ?: 0,
                optionValues = itemOptionValues.map{ ItemOptionValueResponse.of(it) }
            )
        }
    }
}