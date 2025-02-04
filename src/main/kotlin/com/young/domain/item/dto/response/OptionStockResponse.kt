package com.young.domain.item.dto.response

import com.young.domain.item.domain.entity.ItemOptionValue

data class OptionStockResponse(
    val id: Long,
    val option: OptionResponse,
    val stock: Long
) {
    companion object {
        fun of(optionValues: List<ItemOptionValue>) = OptionStockResponse(
            id = optionValues.firstOrNull()?.itemOption?.id ?: 0,
            option = OptionResponse.of(optionValues),
            stock = optionValues.firstOrNull()?.itemOption?.stock ?: 0
        )
    }
}