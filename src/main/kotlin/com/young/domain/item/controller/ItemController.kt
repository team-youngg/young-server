package com.young.domain.item.controller

import com.young.domain.item.dto.request.CreateItemRequest
import com.young.domain.item.dto.request.UpdateItemRequest
import com.young.domain.item.dto.request.UpdateStockRequest
import com.young.domain.item.service.ItemService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@Tag(name = "상품", description = "상품 api")
@RestController
@RequestMapping("/items")
class ItemController (
    private val itemService: ItemService
) {
    @Operation(summary = "삼품 생성", description = "상품을 생성합니다.")
    @PostMapping
    fun createItem(@RequestBody request: CreateItemRequest) = itemService.createItem(request)

    @Operation(summary = "상품 상세 조회", description =  "상품을 상세 조회합니다.")
    @GetMapping("/{itemId}")
    fun getItem(@PathVariable itemId: Long) = itemService.getItem(itemId)

    @Operation(summary = "상품 전체 조회", description = "상품을 전체 조회합니다.")
    @GetMapping
    fun getItems(@PageableDefault pageable: Pageable) = itemService.getItems(pageable)

    // TODO option controller 로 이동
    @Operation(summary = "재고 수정", description = "상품 옵션 아이디로 재고를 수정합니다.")
    @PatchMapping("/stock/{itemOptionId}")
    fun updateStock(@RequestBody request: UpdateStockRequest, @PathVariable itemOptionId: Long)
    = itemService.updateStock(request, itemOptionId)

    @Operation(summary = "상품 카테고리별 조회", description = "상품을 카테고리별로 조회합니다.")
    @GetMapping("/category/{categoryId}")
    fun getItems(@PathVariable categoryId: Long, @PageableDefault pageable: Pageable)
    = itemService.getItemsByCategory(categoryId, pageable)

    @Operation(summary = "상품 수정", description = "상품 정보를 수정합니다.")
    @PatchMapping("/{itemId}")
    fun updateItem(@RequestBody request: UpdateItemRequest, @PathVariable itemId: Long)
    = itemService.updateItem(request, itemId)

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @DeleteMapping("/{itemId}")
    fun deleteItem(@PathVariable itemId: Long)
    = itemService.deleteItem(itemId)
}