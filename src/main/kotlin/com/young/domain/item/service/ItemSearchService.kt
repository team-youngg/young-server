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
import java.util.LinkedList
import java.util.Queue

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

    // 기존 로직에서 카테고리 트리 전체 id를 조회하는 메서드
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
    fun searchItemsByGenderAndItem(
        gender: String,
        item: String,
        minPrice: Long?,
        maxPrice: Long?,
        pageable: Pageable
    ): PageResponse<List<ItemResponse>> {
        val user = securityHolder.user

        // 카테고리 방식 유지: item(검색어)를 포함하는 카테고리 목록 조회
        val categories = categoryRepository.findByNameContainingIgnoreCase(item)
        if (categories.isEmpty()) {
            return PageResponse.of(Page.empty())
        }

        val categoryIds = categories.flatMap { getAllSubCategoryIds(it.id!!) }.distinct()

        // gender가 "none"이면 성별 조건 없이, 아니라면 아이템의 gender 필드도 조건에 추가
        val itemCategories = when {
            minPrice != null && maxPrice != null -> {
                if (gender.equals("none", ignoreCase = true)) {
                    itemCategoryRepository.findByCategoryIdInAndItemPriceBetween(categoryIds, minPrice, maxPrice, pageable)
                } else {
                    itemCategoryRepository.findByCategoryIdInAndItemGenderAndItemPriceBetween(categoryIds, gender, minPrice, maxPrice, pageable)
                }
            }
            minPrice != null -> {
                if (gender.equals("none", ignoreCase = true)) {
                    itemCategoryRepository.findByCategoryIdInAndItemPriceGreaterThanEqual(categoryIds, minPrice, pageable)
                } else {
                    itemCategoryRepository.findByCategoryIdInAndItemGenderAndItemPriceGreaterThanEqual(categoryIds, gender, minPrice, pageable)
                }
            }
            maxPrice != null -> {
                if (gender.equals("none", ignoreCase = true)) {
                    itemCategoryRepository.findByCategoryIdInAndItemPriceLessThanEqual(categoryIds, maxPrice, pageable)
                } else {
                    itemCategoryRepository.findByCategoryIdInAndItemGenderAndItemPriceLessThanEqual(categoryIds, gender, maxPrice, pageable)
                }
            }
            else -> {
                if (gender.equals("none", ignoreCase = true)) {
                    itemCategoryRepository.findByCategoryIdIn(categoryIds, pageable)
                } else {
                    itemCategoryRepository.findByCategoryIdInAndItemGender(categoryIds, gender, pageable)
                }
            }
        }

        val items = itemCategories.map { itemUtil.toItemResponse(it.item, user) }
        return PageResponse.of(items)
    }
}