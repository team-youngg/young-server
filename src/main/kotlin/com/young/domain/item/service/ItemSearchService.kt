package com.young.domain.item.service

import com.young.domain.category.domain.entity.Category
import com.young.domain.category.repository.CategoryRepository
import com.young.domain.category.repository.ItemCategoryRepository
import com.young.domain.item.dto.response.ItemResponse
import com.young.domain.item.repository.ItemRepository
import com.young.domain.item.util.ItemUtil
import com.young.global.common.PageResponse
import com.young.global.security.SecurityHolder
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ItemSearchService (
    private val securityHolder: SecurityHolder,
    private val itemRepository: ItemRepository,
    private val itemUtil: ItemUtil,
    private val itemCategoryRepository: ItemCategoryRepository,
    private val categoryRepository: CategoryRepository
) {
    @Transactional(readOnly = true)
    fun searchItems(query: String, pageable: Pageable): PageResponse<List<ItemResponse>> {
        val user = securityHolder.user
        val items = itemRepository
            .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, pageable)
            .map { itemUtil.toItemResponse(it, user) }
        return PageResponse.of(items)
    }

    private fun getAllSubCategoryIds(categoryId: Long): List<Long> {
        val categoryIds = mutableSetOf(categoryId)
        val category = categoryRepository.findById(categoryId).orElse(null) ?: return categoryIds.toList()
        var currentCategory: Category? = category
        var isFromTotal = false

        while (currentCategory?.parentId != null) {
            if (currentCategory.parentId == 3L) {
                isFromTotal = true
                break
            }
            currentCategory = categoryRepository.findById(currentCategory.parentId!!).orElse(null)
        }

        if (isFromTotal) {
            listOf(1L, 2L, 3L).forEach { genderCategoryId ->
                categoryRepository.findByParentId(genderCategoryId)
                    .firstOrNull { it.name == category.name }
                    ?.let { categoryIds.add(it.id!!) }
            }
        }

        val queue: Queue<Long> = LinkedList(categoryIds)
        while (queue.isNotEmpty()) {
            val currentId = queue.poll()
            val subCategories = categoryRepository.findByParentId(currentId)
            subCategories.forEach {
                if (categoryIds.add(it.id!!)) queue.add(it.id)
            }
        }

        return categoryIds.toList()
    }

    @Transactional(readOnly = true)
    fun getItemsByCategoryAndPrice(
        categoryId: Long,
        minPrice: Long?,
        maxPrice: Long?,
        pageable: Pageable
    ): PageResponse<List<ItemResponse>> {
        val user = securityHolder.user
        val categoryIds = getAllSubCategoryIds(categoryId)

        val itemCategories = when {
            minPrice != null && maxPrice != null ->
                itemCategoryRepository.findByCategoryIdInAndItemPriceBetween(categoryIds, minPrice, maxPrice, pageable)
            minPrice != null ->
                itemCategoryRepository.findByCategoryIdInAndItemPriceGreaterThanEqual(categoryIds, minPrice, pageable)
            maxPrice != null ->
                itemCategoryRepository.findByCategoryIdInAndItemPriceLessThanEqual(categoryIds, maxPrice, pageable)
            else -> itemCategoryRepository.findByCategoryIdIn(categoryIds, pageable)
        }

        val items = itemCategories
            .map { itemUtil.toItemResponse(it.item, user) }

        return PageResponse.of(items)
    }
}