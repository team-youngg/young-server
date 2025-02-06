package com.young.domain.category.dto.response

import com.young.domain.category.domain.entity.Category

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