package com.young.domain.item.util

import com.young.domain.item.domain.entity.Category
import com.young.domain.item.domain.entity.Item
import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.*
import com.young.global.exception.CustomException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ItemUtil (
    private val itemImageRepository: ItemImageRepository,
    private val itemCategoryRepository: ItemCategoryRepository,
    private val itemOptionRepository: ItemOptionRepository,
    private val itemOptionValueRepository: ItemOptionValueRepository,
    private val categoryRepository: CategoryRepository
) {
    @Transactional
    fun getItemElements(item: Item) : ItemElements {
        val images = itemImageRepository.findAllByItem(item).map { it.url }
        val options = itemOptionRepository.findAllByItem(item)
        val optionValues = options.flatMap { itemOptionValueRepository.findAllByItemOption(it) }
        val categories = getCategories(itemCategoryRepository.findByItem(item)?.categoryId
            ?: throw CustomException(ItemError.CATEGORY_NOT_FOUND)
        )

        return ItemElements(
            images,
            options,
            optionValues,
            categories
        )
    }

    fun getCategories(categoryId: Long): List<Category> {
        val categories = mutableListOf<Category>()
        var currentCategory = categoryRepository.findById(categoryId).orElse(null)

        while (currentCategory != null) {
            categories.add(currentCategory)
            currentCategory = currentCategory.parentId?.let { categoryRepository.findById(it).orElse(null) }
        }

        return categories
    }
}