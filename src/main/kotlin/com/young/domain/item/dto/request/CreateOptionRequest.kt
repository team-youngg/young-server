package com.young.domain.item.dto.request

data class CreateOptionRequest(
    val optionValues: Set<CreateOptionValueRequest>
)