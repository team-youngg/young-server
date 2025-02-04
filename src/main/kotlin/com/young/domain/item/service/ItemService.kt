package com.young.domain.item.service

import com.young.domain.item.domain.entity.*
import com.young.domain.item.dto.request.CreateItemRequest
import com.young.domain.item.dto.request.UpdateStockRequest
import com.young.domain.item.dto.response.ImageResponse
import com.young.domain.item.dto.response.ItemDetailResponse
import com.young.domain.item.dto.response.ItemResponse
import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.*
import com.young.domain.item.util.ItemUtil
import com.young.domain.wish.repository.WishRepository
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.File
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
    fun updateStock(request: UpdateStockRequest, itemOptionId: Long) {
        val itemOption = itemOptionRepository.findByIdOrNull(itemOptionId)
            ?: throw CustomException(ItemError.OPTION_NOT_FOUND)

        if (request.isPlus) itemOption.stock += request.count
        else itemOption.stock -= request.count

        itemOptionRepository.save(itemOption)
    }

    fun getItem(id: Long): ItemDetailResponse {
        val item = itemRepository.findByIdOrNull(id) ?: throw CustomException(ItemError.ITEM_NOT_FOUND)
        val itemElements = itemUtil.getItemElements(item)
        return ItemDetailResponse.of(
            item,
            itemElements.images,
            itemElements.options,
            itemElements.optionValues,
            itemElements.categories
        )
    }

    @Transactional
    fun getItems(pageable: Pageable): List<ItemResponse> {
        val user = securityHolder.user
        val wishItemIds: Set<Long> = if (user != null) {
            wishRepository.findItemIdsByUser(user).toSet()
        } else {
            emptySet()
        }
        val items = itemRepository.findAllByOrderByCreatedAtDesc(pageable).toList()

        return items.map { item ->
            val itemElements = itemUtil.getItemElements(item)

            ItemResponse.of(
                item,
                itemElements.images,
                itemElements.options,
                itemElements.optionValues,
                itemElements.categories,
                isWish = user != null && item.id in wishItemIds
            )
        }
    }


    @Transactional
    fun uploadImage(file: MultipartFile) : ImageResponse {
        val filename = "${UUID.randomUUID()}-${file.originalFilename}"

        val directory = File(uploadDir)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val targetFile = File(directory, filename)
        file.transferTo(targetFile)

        return ImageResponse(
            ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/uploads/")
            .path(filename)
            .toUriString()
        )
    }

    @Transactional
    fun createCategory() {
        // TODO 카테고리 생성(어드민일걸?)
    }
}