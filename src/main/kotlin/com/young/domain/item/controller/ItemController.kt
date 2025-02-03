package com.young.domain.item.controller

import com.young.domain.item.dto.request.CreateItemRequest
import com.young.domain.item.dto.request.UpdateStockRequest
import com.young.domain.item.service.ItemService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/items")
class ItemController (
    private val itemService: ItemService
) {
    @PostMapping
    fun createItem(@RequestBody request: CreateItemRequest) = itemService.createItem(request)

    @PostMapping("/images", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(@RequestPart image: MultipartFile) = itemService.uploadImage(image)

    @GetMapping("/{itemId}")
    fun getItem(@PathVariable itemId: Long) = itemService.getItem(itemId)

    @GetMapping
    fun getItems(@PageableDefault pageable: Pageable) = itemService.getItems(pageable)

    @PatchMapping("/{itemOptionId}")
    fun updateStock(@RequestBody request: UpdateStockRequest, @PathVariable itemOptionId: Long)
    = itemService.updateStock(request, itemOptionId)

    // TODO 아이템 수정 삭제, 아이템이미지 수정 삭제, 아이템 옵션 수정삭제
}