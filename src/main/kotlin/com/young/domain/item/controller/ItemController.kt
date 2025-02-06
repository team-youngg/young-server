package com.young.domain.item.controller

import com.young.domain.item.domain.entity.Item
import com.young.domain.item.dto.request.CreateItemRequest
import com.young.domain.item.dto.request.UpdateStockRequest
import com.young.domain.item.service.ItemService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "상품", description = "상품 api")
@RestController
@RequestMapping("/items")
class ItemController (
    private val itemService: ItemService
) {
    @Operation(summary = "삼품 생성", description = "상품을 생성합니다.")
    @PostMapping
    fun createItem(@RequestBody request: CreateItemRequest) = itemService.createItem(request)

    @Operation(summary = "이미지 업로드", description = "이미지를 업로드 합니다.")
    @PostMapping("/images", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(@RequestPart image: MultipartFile) = itemService.uploadImage(image)

    @Operation(summary = "상품 상세 조회", description =  "상품을 상세 조회합니다.")
    @GetMapping("/{itemId}")
    fun getItem(@PathVariable itemId: Long) = itemService.getItem(itemId)

    @Operation(summary = "상품 전체 조회", description = "상품을 전체 조회합니다.")
    @GetMapping
    fun getItems(@PageableDefault pageable: Pageable) = itemService.getItems(pageable)

    @Operation(summary = "재고 수정", description = "상품 옵션 아이디로 재고를 수정합니다.")
    @PatchMapping("/{itemOptionId}")
    fun updateStock(@RequestBody request: UpdateStockRequest, @PathVariable itemOptionId: Long)
    = itemService.updateStock(request, itemOptionId)

    @GetMapping("/category/{categoryId}")
    fun getItems(@PathVariable categoryId: Long, @RequestParam pageable: Pageable): List<Item> {
        return itemService.getItemsByCategory(categoryId, pageable)
    }

    // TODO 아이템 수정 삭제, 아이템이미지 수정 삭제, 아이템 옵션 수정삭제
}