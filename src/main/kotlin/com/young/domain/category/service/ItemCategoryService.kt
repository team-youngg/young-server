package com.young.domain.category.service

import com.young.domain.category.domain.entity.Category
import com.young.domain.category.domain.entity.ItemCategory
import com.young.domain.category.dto.response.CategoryResponse
import com.young.domain.category.repository.CategoryRepository
import com.young.domain.category.repository.ItemCategoryRepository
import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.ItemRepository
import com.young.global.exception.CustomException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ItemCategoryService (
    private val categoryRepository: CategoryRepository,
    private val itemCategoryRepository: ItemCategoryRepository,
    private val itemRepository: ItemRepository
) {
    @Transactional
    fun createCategory(name: String, parentId: Long?) {
        val category = Category(name = name, parentId = parentId)
        categoryRepository.save(category)
    }

    @Transactional
    fun deleteCategory(categoryId: Long) {
        itemCategoryRepository.deleteAllByCategoryId(categoryId)
        categoryRepository.deleteById(categoryId)
    }

    @Transactional
    fun assignItemToCategory(itemId: Long, categoryId: Long): ItemCategory {
        val item = itemRepository.findById(itemId)
            .orElseThrow { CustomException(ItemError.ITEM_NOT_FOUND) }
        categoryRepository.findById(categoryId)
            .orElseThrow { CustomException(ItemError.CATEGORY_NOT_FOUND) }
        val mapping = ItemCategory(item = item, categoryId = categoryId)
        return itemCategoryRepository.save(mapping)
    }

    @Transactional
    fun removeItemFromCategory(itemId: Long, categoryId: Long) {
        val mapping = itemCategoryRepository.findByItemIdAndCategoryId(itemId, categoryId)
            ?: throw CustomException(ItemError.CATEGORY_NOT_MATCH)
        itemCategoryRepository.delete(mapping)
    }

    @Transactional(readOnly = true)
    fun getCategories(): List<CategoryResponse> {
        return categoryRepository.findAll().sortedBy { it.id }.map { CategoryResponse.of(it) }
    }

    @Transactional(readOnly = true)
    fun getMergedCategories(): List<com.young.domain.category.service.CategoryResponse> {
        // 모든 카테고리 조회
        val allCategories = categoryRepository.findAll()

        // 성별 카테고리 id (예: 여성, 남성, 전체)
        val genderCategoryIds = setOf(1L, 2L, 3L)

        // 상위 카테고리: parentId가 성별 카테고리인 경우
        val topCategories = allCategories.filter { it.parentId in genderCategoryIds }

        // 동일한 이름의 상위 카테고리끼리 그룹화 (예: 여러 성별에 걸쳐 "상의"가 있을 경우)
        val groupedTop = topCategories.groupBy { it.name }

        return groupedTop.map { (topName, topList) ->
            // 여러 상위 카테고리의 id들을 모음
            val topIds = topList.mapNotNull { it.id }
            // 해당 상위 카테고리들 아래에 속하는 하위 카테고리 조회
            val childCategories = allCategories.filter { it.parentId in topIds }
            // 동일한 이름의 하위 카테고리끼리 합침 (대표 id는 가장 작은 id 사용)
            val groupedChildren = childCategories.groupBy { it.name }
            val mergedChildren = groupedChildren.map { (childName, children) ->
                SubCategoryResponse(
                    id = children.minOf { it.id!! },
                    name = childName
                )
            }
            CategoryResponse(name = topName, children = mergedChildren)
        }
    }

    @Transactional
    fun createSubCategoryForAllGenders(parentCategoryName: String, subCategoryName: String) {
        // 기본 카테고리 id (여성, 남성, 전체로 가정: 1, 2, 3)
        val genderBaseIds = listOf(1L, 2L, 3L)
        for (baseId in genderBaseIds) {
            // 기본 카테고리 아래에서 상위 카테고리("상의") 찾기
            val parentCategory = categoryRepository.findByParentIdAndName(baseId, parentCategoryName)
                ?: throw IllegalStateException("Parent category '$parentCategoryName' not found under base id $baseId")
            // 상위 카테고리 아래에 하위 카테고리("셔츠") 추가
            categoryRepository.save(Category(name = subCategoryName, parentId = parentCategory.id))
        }
    }
}

data class CategoryResponse(
    val name: String,
    val children: List<SubCategoryResponse>
)

data class SubCategoryResponse(
    val id: Long,
    val name: String
)