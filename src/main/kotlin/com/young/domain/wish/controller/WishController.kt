package com.young.domain.wish.controller

import com.young.domain.wish.service.WishService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "찜", description = "찜 api")
@RestController
@RequestMapping("/wish")
class WishController (
    private val wishService: WishService
) {
    @Operation(summary = "찜하기", description = "찜하기 또는 찜 취소하기")
    @PostMapping("/{itemId}")
    fun updateWish(@PathVariable itemId: Long) = wishService.updateWishItem(itemId)

    @Operation(summary = "찜 목록 조회", description = "찜 목록 조회하기")
    @GetMapping
    fun getWish() = wishService.getWishList()
}