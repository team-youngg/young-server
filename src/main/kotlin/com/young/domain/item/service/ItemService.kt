package com.young.domain.item.service

import com.young.domain.item.domain.entity.Image
import com.young.domain.item.domain.entity.Item
import com.young.domain.item.dto.request.CreateItemRequest
import com.young.domain.item.dto.response.ItemResponse
import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.ImageRepository
import com.young.domain.item.repository.ItemRepository
import com.young.global.exception.CustomException
import org.springframework.beans.factory.annotation.Value
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
    private val imageRepository: ImageRepository
) {
    @Transactional
    fun createItem(request: CreateItemRequest, files: List<MultipartFile>) {
        val item = Item(
            name = request.name,
            description = request.description,
            price = request.price,
            stock = request.stock,
        )

        itemRepository.save(item)

        for (file in files) {
            uploadImage(file, item.id!!)
        }
    }

    fun getItem(id: Long): ItemResponse {
        val item = itemRepository.findByIdOrNull(id) ?: throw CustomException(ItemError.ITEM_NOT_FOUND)
        val images = getImages(id)

        return ItemResponse.of(item, images)
    }

//    fun getItems(): List<ItemResponse> {
//
//    }

    fun uploadImage(file: MultipartFile, itemId: Long) {
        val filename = "${UUID.randomUUID()}-${file.originalFilename}"

        val directory = File(uploadDir)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val targetFile = File(directory, filename)
        file.transferTo(targetFile)

        val item = itemRepository.findByIdOrNull(itemId) ?: throw CustomException(ItemError.ITEM_NOT_FOUND)

        val image = Image(url = filename, item = item)
        imageRepository.save(image)
    }

    fun getImages(itemId: Long): List<String> {
        val images = imageRepository.findAllByItemId(itemId)

        return images.map { image ->
            ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(image.url)
                .toUriString()
        }
    }
}