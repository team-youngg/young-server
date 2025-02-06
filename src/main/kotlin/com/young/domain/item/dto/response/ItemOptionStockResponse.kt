package com.young.domain.item.dto.response

import com.young.domain.option.domain.entity.ItemOptionValue

data class ItemOptionStockResponse(
    val id: Long,
    val option: ItemOptionResponse,
    val stock: Long
) {
    companion object {
        fun of(optionValues: List<ItemOptionValue>) = ItemOptionStockResponse(
            id = optionValues.firstOrNull()?.itemOption?.id ?: 0,
            option = ItemOptionResponse.of(optionValues),
            stock = optionValues.firstOrNull()?.itemOption?.stock ?: 0
        )
    }
}