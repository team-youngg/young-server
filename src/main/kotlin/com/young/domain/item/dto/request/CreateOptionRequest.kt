package com.young.domain.item.dto.request

import com.young.domain.item.domain.enums.ItemColor
import com.young.domain.item.domain.enums.ItemSize

data class CreateOptionRequest(
    val size: ItemSize,
    val color: ItemColor
)