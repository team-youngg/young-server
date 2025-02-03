package com.young.domain.item.service

import com.young.domain.item.domain.entity.ItemImage
import com.young.domain.item.domain.entity.Item
import com.young.domain.item.domain.entity.ItemColor
import com.young.domain.item.domain.entity.ItemOption
import com.young.domain.item.dto.request.CreateItemRequest
import com.young.domain.item.dto.request.UpdateStockRequest
import com.young.domain.item.dto.response.ImageResponse
import com.young.domain.item.dto.response.ItemResponse
import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.ItemColorRepository
import com.young.domain.item.repository.ItemImageRepository
import com.young.domain.item.repository.ItemOptionRepository
import com.young.domain.item.repository.ItemRepository
import com.young.global.exception.CustomException
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
    private val itemColorRepository: ItemColorRepository,
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

        val itemImages = request.images.map { imageUrl ->
            ItemImage(url = imageUrl, item = item)
        }
        itemImageRepository.saveAll(itemImages)

        val itemOptions = request.options.map { option ->
            ItemOption(
                item = item,
                color = createColor(option.color, option.hex),
                size = option.size,
                stock = 0
            )
        }
        itemOptionRepository.saveAll(itemOptions)

        // TODO 카테고리 저장
    }

    @Transactional
    fun updateStock(request: UpdateStockRequest, itemOptionId: Long) {
        val itemOption = itemOptionRepository.findByIdOrNull(itemOptionId)
            ?: throw CustomException(ItemError.OPTION_NOT_FOUND)

        if (request.isPlus) itemOption.stock += request.count
        else itemOption.stock -= request.count

        itemOptionRepository.save(itemOption)
    }

    fun getItem(id: Long): ItemResponse {
        val item = itemRepository.findByIdOrNull(id) ?: throw CustomException(ItemError.ITEM_NOT_FOUND)
        val images = itemImageRepository.findAllByItem(item).map { it.url }
        val options = itemOptionRepository.findAllByItem(item)
        return ItemResponse.of(item, images, options)
    }

    fun getItems(pageable: Pageable) : List<ItemResponse> {
        val items = itemRepository.findAllByOrderByCreatedAtDesc(pageable).toList()
        return items.map { item ->
            val images = itemImageRepository.findAllByItem(item).map { it.url }
            val options = itemOptionRepository.findAllByItem(item)
            ItemResponse.of(item, images, options)
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
    fun createColor(color: String, hex: String) : ItemColor {
        if (!itemColorRepository.existsByColorAndHex(color, hex)) {
            val itemColor = ItemColor(
                color = color,
                hex = hex,
            )
            return itemColorRepository.save(itemColor)
        } else {
            return itemColorRepository.findByColorAndHex(color, hex)!!
        }
    }
}