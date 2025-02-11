package com.young.domain.item.controller

import com.young.domain.item.dto.response.ItemResponse
import com.young.domain.item.service.ItemSearchService
import com.young.domain.item.service.ItemService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "상품 검색", description = "상품 검색 api")
@RestController
@RequestMapping("/items/search")
class ItemSearchController(
    private val itemSearchService: ItemSearchService
) {
    @Operation(summary = "키워드 검색", description = "상품 이름과 설명의 키워드로 검색합니다.")
    @GetMapping
    fun searchItems(@RequestParam("q") query: String, @PageableDefault pageable: Pageable): List<ItemResponse>
    = itemSearchService.searchItems(query, pageable)

    @Operation(summary = "가격 검색", description = "가격대로 검색합니다.")
    @GetMapping("/price")
    fun searchItemsByPrice(
        @RequestParam(value = "min", required = false) minPrice: Long?,
        @RequestParam(value = "max", required = false) maxPrice: Long?,
        @PageableDefault pageable: Pageable
    ) = itemSearchService.searchItemsByPrice(minPrice, maxPrice, pageable)
}