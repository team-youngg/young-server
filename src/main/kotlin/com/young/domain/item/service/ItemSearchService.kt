package com.young.domain.item.service

import com.young.domain.category.domain.entity.Category
import com.young.domain.category.repository.CategoryRepository
import com.young.domain.category.repository.ItemCategoryRepository
import com.young.domain.item.dto.response.ItemResponse
import com.young.domain.item.repository.ItemRepository
import com.young.domain.item.util.ItemUtil
import com.young.global.common.PageResponse
import com.young.global.security.SecurityHolder
import org.springframework.data.domain.Page
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

    @Transactional(readOnly = true)
    fun searchItemsByGenderAndItem(
        gender: String,
        item: String,
        minPrice: Long?,
        maxPrice: Long?,
        pageable: Pageable
    ): PageResponse<List<ItemResponse>> {
        val user = securityHolder.user

        // 1. gender 값에 따라 기준 카테고리 ID 결정
        val baseCategoryIds: List<Long> = when (gender.toLowerCase()) {
            "men" -> listOf(1L)
            "women" -> listOf(2L)
            "none" -> categoryRepository.findAll().mapNotNull { it.id }
            else -> return PageResponse.of(Page.empty())
        }

        // 2. 기준 카테고리(들)에 속하는 하위 카테고리 ID 목록 조회
        val allBaseCategoryIds = if (gender.equals("none", ignoreCase = true)) {
            baseCategoryIds
        } else {
            baseCategoryIds.flatMap { baseId -> getAllSubCategoryIds(baseId) }
        }

        // 3. 기준 범위 내에서, Category.name에 item 키워드(예: "hoodie")가 포함된 후보 카테고리 조회
        val candidateCategories = if (gender.equals("none", ignoreCase = true)) {
            categoryRepository.findByNameContainingIgnoreCase(item)
        } else {
            categoryRepository.findByIdInAndNameContainingIgnoreCase(allBaseCategoryIds, item)
        }

        if (candidateCategories.isEmpty()) {
            return PageResponse.of(Page.empty())
        }

        // 4. 후보 카테고리 각각에 대해, 해당 카테고리와 하위 카테고리들의 ID 목록 산출
        val finalCategoryIds = candidateCategories.flatMap { candidate ->
            getAllSubCategoryIds(candidate.id!!)
        }.distinct()

        // 5. 최종 카테고리 ID에 해당하는 아이템들을 가격 조건과 함께 DB에서 페이징 조회
        val itemCategories = when {
            minPrice != null && maxPrice != null ->
                itemCategoryRepository.findByCategoryIdInAndItemPriceBetween(finalCategoryIds, minPrice, maxPrice, pageable)
            minPrice != null ->
                itemCategoryRepository.findByCategoryIdInAndItemPriceGreaterThanEqual(finalCategoryIds, minPrice, pageable)
            maxPrice != null ->
                itemCategoryRepository.findByCategoryIdInAndItemPriceLessThanEqual(finalCategoryIds, maxPrice, pageable)
            else ->
                itemCategoryRepository.findByCategoryIdIn(finalCategoryIds, pageable)
        }

        val items = itemCategories.map { itemUtil.toItemResponse(it.item, user) }

        return PageResponse.of(items)
    }
}