package com.young.domain.item.util

import com.young.domain.category.domain.entity.Category
import com.young.domain.option.domain.entity.ItemOption
import com.young.domain.option.domain.entity.ItemOptionValue

data class ItemElements(
    val images: List<String>,
    val options: List<ItemOption>,
    val optionValues: List<ItemOptionValue>,
    val categories: List<Category>
)
