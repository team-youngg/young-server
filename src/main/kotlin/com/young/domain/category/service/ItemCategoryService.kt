package com.young.domain.category.service

import com.young.domain.category.repository.ItemCategoryRepository
import com.young.domain.item.repository.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ItemCategoryService (
    private val itemCategoryRepository: ItemCategoryRepository,
    private val itemRepository: ItemRepository,
    private val categoryRepository: ItemCategoryRepository
) {
    @Transactional
    fun createCategory() {
        // TODO 카테고리 생성(어드민일걸?)
    }
}