package com.young.domain.order.dto.response

import com.young.domain.option.domain.entity.ItemOptionValue
import com.young.domain.item.dto.response.ItemOptionResponse
import com.young.domain.order.domain.entity.OrderItem
import com.young.domain.order.domain.entity.OrderItemOption

data class OrderItemResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Long,
    val option: ItemOptionResponse,
    val images: List<String>,
    val count: Long
) {
    companion object {
        fun of(orderItem: OrderItem, images: List<String>, option: OrderItemOption,
               optionValues: List<ItemOptionValue>): OrderItemResponse {
            return OrderItemResponse(
                id = orderItem.item.id!!,
                name = orderItem.item.name,
                description = orderItem.item.description,
                price = orderItem.price,
                option = ItemOptionResponse.of(optionValues),
                images = images,
                count = option.count,
            )
        }
    }
}