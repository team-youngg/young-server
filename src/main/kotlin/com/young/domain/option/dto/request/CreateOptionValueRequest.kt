package com.young.domain.option.dto.request

import com.young.domain.option.domain.enums.ItemOptionType

data class CreateOptionValueRequest(
    val type: ItemOptionType,
    val value: String,
    val detail: String? = null,
)