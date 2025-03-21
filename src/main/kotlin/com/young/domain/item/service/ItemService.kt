package com.young.domain.item.service

import com.young.domain.category.domain.entity.Category
import com.young.domain.category.domain.entity.ItemCategory
import com.young.domain.category.repository.CategoryRepository
import com.young.domain.category.repository.ItemCategoryRepository
import com.young.domain.image.domain.entity.ItemImage
import com.young.domain.image.repository.ItemImageRepository
import com.young.domain.item.domain.entity.*
import com.young.domain.item.dto.request.CreateItemRequest
import com.young.domain.item.dto.request.UpdateItemRequest
import com.young.domain.item.dto.request.UpdateStockRequest
import com.young.domain.item.dto.response.ItemDetailResponse
import com.young.domain.item.dto.response.ItemResponse
import com.young.domain.item.dto.response.StockResponse
import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.*
import com.young.domain.item.util.ItemUtil
import com.young.domain.option.domain.entity.ItemOption
import com.young.domain.option.domain.entity.ItemOptionValue
import com.young.domain.option.repository.ItemOptionRepository
import com.young.domain.option.repository.ItemOptionValueRepository
import com.young.domain.wish.repository.WishRepository
import com.young.global.common.PageResponse
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ItemService (
    @Value("\${spring.upload.dir}") private val uploadDir: String,
    private val itemRepository: ItemRepository,
    private val itemImageRepository: ItemImageRepository,
    private val itemOptionRepository: ItemOptionRepository,
    private val categoryRepository: CategoryRepository,
    private val itemCategoryRepository: ItemCategoryRepository,
    private val itemOptionValueRepository: ItemOptionValueRepository,
    private val itemUtil: ItemUtil,
    private val securityHolder: SecurityHolder,
    private val wishRepository: WishRepository
) {
    @Transactional
    fun createItem(request: CreateItemRequest) {
        val item = Item(
            name = request.name,
            description = request.description,
            price = request.price,
            detail = request.detail,
            gender = request.gender,
        )
        itemRepository.save(item)

        if (!categoryRepository.existsById(request.categoryId)) throw CustomException(ItemError.CATEGORY_NOT_FOUND)
        val itemCategory = ItemCategory(
            item = item,
            categoryId = request.categoryId,
        )
        itemCategoryRepository.save(itemCategory)

        val itemImages = request.images.map { imageUrl ->
            ItemImage(url = imageUrl, item = item)
        }
        itemImageRepository.saveAll(itemImages)

        request.options.forEach { optionRequest ->
            val itemOption = itemOptionRepository.save(
                ItemOption(
                    item = item,
                    stock = 0
                )
            )

            val optionValues = optionRequest.optionValues.map { optionValueRequest ->
                ItemOptionValue(
                    itemOption = itemOption,
                    type = optionValueRequest.type,
                    value = optionValueRequest.value,
                    detail = optionValueRequest.detail
                )
            }
            itemOptionValueRepository.saveAll(optionValues)
        }
    }

    @Transactional
    fun updateStock(request: UpdateStockRequest, itemOptionId: Long): StockResponse {
        val itemOption = itemOptionRepository.findByIdOrNull(itemOptionId)
            ?: throw CustomException(ItemError.OPTION_NOT_FOUND)

        if (itemOption.stock + request.count < 0) throw CustomException(ItemError.STOCK_UNDER_ZERO)

        itemOption.stock += request.count
        itemOptionRepository.save(itemOption)

        return StockResponse(itemOption.stock)
    }

    fun getItem(id: Long): ItemDetailResponse {
        val user = securityHolder.user
        val item = itemRepository.findByIdOrNull(id) ?: throw CustomException(ItemError.ITEM_NOT_FOUND)
        val itemElements = itemUtil.getItemElements(item)
        val wishItemIds: Set<Long> = if (user != null) {
            wishRepository.findItemIdsByUser(user).toSet()
        } else {
            emptySet()
        }
        return ItemDetailResponse.of(
            item,
            itemElements.images,
            itemElements.options,
            itemElements.optionValues,
            itemElements.categories,
            isWish = user != null && item.id in wishItemIds
        )
    }

    @Transactional
    fun getItems(pageable: Pageable): PageResponse<List<ItemResponse>> {
        val user = securityHolder.user
        val items = itemRepository.findAllByOrderByCreatedAtDesc(pageable)
            .map { itemUtil.toItemResponse(it, user) }

        return PageResponse.of(items)
    }

    @Transactional
    fun updateItem(request: UpdateItemRequest, itemId: Long) {
        val item = itemRepository.findByIdOrNull(itemId) ?: throw CustomException(ItemError.ITEM_NOT_FOUND)

        item.name = request.name ?: item.name
        item.description = request.description ?: item.description
        item.price = request.price ?: item.price
        item.detail = request.detail ?: item.detail

        // TODO category 랑 이미지 수정도 만들기

        itemRepository.save(item)
    }

    @Transactional
    fun deleteItem(itemId: Long) {
        val item = itemRepository.findByIdOrNull(itemId) ?: throw CustomException(ItemError.ITEM_NOT_FOUND)
        itemRepository.delete(item)
    }
}