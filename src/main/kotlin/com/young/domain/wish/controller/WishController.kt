package com.young.domain.wish.controller

import com.young.domain.wish.service.WishService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/wish")
class WishController (
    private val wishService: WishService
) {
    @PostMapping("/{itemId}")
    fun updateWish(@PathVariable itemId: Long) = wishService.updateWishItem(itemId)

    @GetMapping
    fun getWish() = wishService.getWishList()
}