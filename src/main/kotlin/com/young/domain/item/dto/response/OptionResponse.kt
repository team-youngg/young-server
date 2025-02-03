package com.young.domain.item.dto.response

import com.young.domain.item.domain.entity.ItemOption
import com.young.domain.item.domain.enums.ItemColor
import com.young.domain.item.domain.enums.ItemSize

data class OptionResponse (
    val color: String,
    val hex: ItemColor,
    val size: ItemSize,
    val stock: Long
) {
    companion object {
        fun of(itemOption: ItemOption) : OptionResponse {
            return OptionResponse(
                color = itemOption.color.color,
                hex = itemOption.color,
                size = itemOption.size,
                stock = itemOption.stock
            )
        }
    }
}