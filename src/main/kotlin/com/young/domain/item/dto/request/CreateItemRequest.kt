package com.young.domain.item.dto.request

data class CreateItemRequest(
    val name: String,
    val description: String,
    val price: Long,
    val stock: Long,
    val options: List<String>? = emptyList()
)