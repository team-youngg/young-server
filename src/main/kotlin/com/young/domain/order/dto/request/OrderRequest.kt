package com.young.domain.order.dto.request

data class OrderRequest (
    val itemOptionId: Long,
    val count: Long,
)