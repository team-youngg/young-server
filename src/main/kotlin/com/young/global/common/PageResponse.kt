package com.young.global.common

import org.springframework.data.domain.Page

data class PageResponse<T>(
    val data: T,
    val totalPages: Int,
    val currentPage: Int
) {
    companion object {
        fun <T> of(page: Page<T>): PageResponse<List<T>> {
            return PageResponse(
                data = page.content,
                totalPages = page.totalPages,
                currentPage = page.number + 1
            )
        }
    }
}
