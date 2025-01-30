package com.young.domain.item.controller

import com.young.domain.item.dto.request.CreateItemRequest
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
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createItem(
        @RequestParam name: String,
        @RequestParam description: String,
        @RequestParam price: Long,
        @RequestParam stock: Long,
        @RequestParam options: List<String>?,
        @RequestPart("files", required = false) files: List<MultipartFile>
    ) {
        val itemData = CreateItemRequest(name, description, price, stock, options)
        itemService.createItem(itemData, files)
    }

    @GetMapping("/{itemId}")
    fun getItem(@PathVariable itemId: Long) = itemService.getItem(itemId)

    @GetMapping
    fun getItems(@PageableDefault pageable: Pageable) = itemService.getItems(pageable)

    // TODO 아이템 수정 삭제, 아이템이미지 수정 삭제, 아이템 옵션 수정삭제
}