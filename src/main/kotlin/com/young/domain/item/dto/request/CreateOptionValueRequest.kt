package com.young.domain.item.dto.request

import com.young.domain.item.domain.enums.ItemOptionType

data class CreateOptionValueRequest(
    val type: ItemOptionType,
    val value: String,
    val detail: String? = null,
)