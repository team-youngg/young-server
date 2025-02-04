package com.young.domain.item.dto.response

import com.young.domain.item.domain.entity.Category

data class CategoryResponse(
    val name: String
) {
    companion object {
        fun of(category: Category): CategoryResponse {
            return CategoryResponse(
                name = category.name
            )
        }
    }
}
