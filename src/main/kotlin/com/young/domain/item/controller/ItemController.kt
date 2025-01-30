package com.young.domain.item.controller

import com.young.domain.item.dto.request.CreateItemRequest
import com.young.domain.item.service.ItemService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/items")
class ItemController (
    private val itemService: ItemService
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createItem(
        @RequestParam name: String,
        @RequestParam description: String,
        @RequestParam price: Long,
        @RequestParam stock: Long,
        @RequestPart("files", required = false) files: List<MultipartFile>
    ) {
        val itemData = CreateItemRequest(name, description, price, stock)
        itemService.createItem(itemData, files)
    }

    @GetMapping("/{itemId}")
    fun getItem(@PathVariable itemId: Long) = itemService.getItem(itemId)

    @GetMapping
    fun getItems() = itemService.getItems()

    // TODO 아이템 수정 삭제, 파일 수정 삭제
}