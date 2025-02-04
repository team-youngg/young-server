package com.young.domain.item.util

import com.young.domain.item.domain.entity.Category
import com.young.domain.item.domain.entity.ItemOption
import com.young.domain.item.domain.entity.ItemOptionValue

data class ItemElements(
    val images: List<String>,
    val options: List<ItemOption>,
    val optionValues: List<ItemOptionValue>,
    val categories: List<Category>
)
