package com.young.domain.item.controller

import com.young.domain.item.dto.response.ItemResponse
import com.young.domain.item.service.ItemSearchService
import com.young.domain.item.service.ItemService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@Tag(name = "상품 검색", description = "상품 검색 api")
@RestController
@RequestMapping("/items")
class ItemSearchController(
    private val itemSearchService: ItemSearchService
) {
    @Operation(summary = "키워드 검색", description = "상품 이름과 설명의 키워드로 검색합니다.")
    @GetMapping("/search")
    fun searchItems(@RequestParam("q") query: String, @PageableDefault pageable: Pageable)
    = itemSearchService.searchItems(query, pageable)

    @Operation(summary = "카테고리 별 검색", description = "카테고리별로 검색하고 가격 조건을 추가하여 필터링합니다.")
    @GetMapping("/category/{categoryId}")
    fun getItemsByCategoryAndPrice(
        @PathVariable categoryId: Long,
        @RequestParam(value = "min", required = false) minPrice: Long?,
        @RequestParam(value = "max", required = false) maxPrice: Long?,
        @PageableDefault pageable: Pageable
    ) = itemSearchService.getItemsByCategoryAndPrice(categoryId, minPrice, maxPrice, pageable)
}