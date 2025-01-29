package com.young.domain.item.controller

import com.young.domain.item.dto.request.CreateItemRequest
import com.young.domain.item.service.ItemService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
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
        @RequestParam price: Int,
        @RequestParam stock: Int,
        @RequestPart("files", required = false) files: List<MultipartFile>
    ) {
        val itemData = CreateItemRequest(name, description, price, stock)
        itemService.createItem(itemData, files)
    }

    // TODO 아이템 조회
    // TODO 아이템 수정 삭제, 파일 수정 삭제
}