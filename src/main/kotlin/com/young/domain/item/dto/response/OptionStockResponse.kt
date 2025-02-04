package com.young.domain.item.dto.response

import com.young.domain.item.domain.entity.ItemOptionValue

data class OptionStockResponse(
    val option: OptionResponse,
    val stock: Long
) {
    companion object {
        fun of(optionValues: List<ItemOptionValue>) = OptionStockResponse(
            option = OptionResponse.of(optionValues),
            stock = optionValues.firstOrNull()?.itemOption?.stock ?: 0
        )
    }
}