package com.young.domain.category.repository

import com.young.domain.item.domain.entity.Item
import com.young.domain.category.domain.entity.ItemCategory
import org.springframework.data.jpa.repository.JpaRepository

interface ItemCategoryRepository : JpaRepository<ItemCategory, Long> {
    fun findByItem(item: Item): ItemCategory?
    fun findByCategoryIdIn(categoryIds: List<Long>): List<ItemCategory>
}