package com.young.domain.option.dto.request

data class CreateOptionRequest(
    val optionValues: Set<CreateOptionValueRequest>
)