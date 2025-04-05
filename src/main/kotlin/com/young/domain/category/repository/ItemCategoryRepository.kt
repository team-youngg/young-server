package com.young.domain.category.repository

import com.young.domain.item.domain.entity.Item
import com.young.domain.category.domain.entity.ItemCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ItemCategoryRepository : JpaRepository<ItemCategory, Long> {
    fun findByItem(item: Item): ItemCategory?
    fun findByCategoryIdIn(categoryIds: List<Long>): List<ItemCategory>

    fun findByCategoryIdInAndItemPriceBetween(
        categoryIds: List<Long>, minPrice: Long, maxPrice: Long, pageable: Pageable
    ): Page<ItemCategory>
    fun findByCategoryIdInAndItemPriceGreaterThanEqual(
        categoryIds: List<Long>, minPrice: Long, pageable: Pageable
    ): Page<ItemCategory>
    fun findByCategoryIdInAndItemPriceLessThanEqual(
        categoryIds: List<Long>, maxPrice: Long, pageable: Pageable): Page<ItemCategory>
    fun findByCategoryIdIn(categoryIds: List<Long>, pageable: Pageable): Page<ItemCategory>

    fun findByItemIdAndCategoryId(itemId: Long, categoryId: Long): ItemCategory?
    fun deleteAllByCategoryId(categoryId: Long)

    fun findByCategoryIdInAndItemGenderAndItemPriceBetween(
        categoryIds: List<Long>,
        gender: String,
        minPrice: Long,
        maxPrice: Long,
        pageable: Pageable
    ): Page<ItemCategory>

    fun findByCategoryIdInAndItemGenderAndItemPriceGreaterThanEqual(
        categoryIds: List<Long>,
        gender: String,
        minPrice: Long,
        pageable: Pageable
    ): Page<ItemCategory>

    fun findByCategoryIdInAndItemGender(
        categoryIds: List<Long>,
        gender: String,
        pageable: Pageable
    ): Page<ItemCategory>

    fun findByCategoryIdInAndItemGenderAndItemPriceLessThanEqual(
        categoryIds: List<Long>,
        gender: String,
        maxPrice: Long,
        pageable: Pageable
    ): Page<ItemCategory>

    fun countByCategoryId(categoryId: Long): Long
}