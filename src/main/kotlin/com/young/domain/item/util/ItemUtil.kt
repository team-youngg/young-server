package com.young.domain.item.util

import com.young.domain.category.domain.entity.Category
import com.young.domain.category.repository.CategoryRepository
import com.young.domain.category.repository.ItemCategoryRepository
import com.young.domain.image.repository.ItemImageRepository
import com.young.domain.item.domain.entity.Item
import com.young.domain.item.dto.response.ItemResponse
import com.young.domain.item.error.ItemError
import com.young.domain.option.repository.ItemOptionRepository
import com.young.domain.option.repository.ItemOptionValueRepository
import com.young.domain.user.domain.entity.User
import com.young.domain.wish.repository.WishRepository
import com.young.global.exception.CustomException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ItemUtil (
    private val itemImageRepository: ItemImageRepository,
    private val itemCategoryRepository: ItemCategoryRepository,
    private val itemOptionRepository: ItemOptionRepository,
    private val itemOptionValueRepository: ItemOptionValueRepository,
    private val categoryRepository: CategoryRepository,
    private val wishRepository: WishRepository,
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

    @Transactional
    fun toItemResponse(item: Item, user: User?): ItemResponse {
        val isWish = user != null && wishRepository.findItemIdsByUser(user).contains(item.id)
        val itemElements = getItemElements(item)

        return ItemResponse.of(
            item,
            itemElements.images,
            itemElements.options,
            itemElements.optionValues,
            itemElements.categories,
            isWish
        )
    }
}