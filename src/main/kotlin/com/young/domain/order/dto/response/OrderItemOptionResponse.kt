package com.young.domain.order.dto.response

import com.young.domain.item.dto.response.ItemOptionValueResponse

data class OrderItemOptionResponse (
    val optionId: Long,
    val optionValues: List<ItemOptionValueResponse>
)