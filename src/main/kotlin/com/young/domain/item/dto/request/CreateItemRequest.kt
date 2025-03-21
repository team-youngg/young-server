package com.young.domain.item.dto.request

import com.young.domain.option.dto.request.CreateOptionRequest

data class CreateItemRequest(
    val name: String,
    val description: String,
    val price: Long,
    val detail: String,
    val categoryId: Long,
    val gender: String,
    val images: Set<String>,
    val options: Set<CreateOptionRequest>
)