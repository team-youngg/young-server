package com.young.domain.item.dto.request

data class CreateItemRequest(
    val name: String,
    val description: String,
    val price: Long,
    val detail: String,
    val categoryId: Long,
    val images: Set<String>,
    val options: Set<CreateOptionRequest>
)