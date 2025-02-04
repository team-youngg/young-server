package com.young.domain.cart.controller

import com.young.domain.cart.dto.request.CreateCartRequest
import com.young.domain.cart.dto.request.UpdateCartRequest
import com.young.domain.cart.service.CartService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/carts")
class CartController (
    private val cartService: CartService
) {
    @PostMapping
    fun createCartItem(@RequestBody request: CreateCartRequest) = cartService.createCartItem(request)

    @PatchMapping("/{cartItemOptionId}")
    fun updateCartItem(@RequestBody request: UpdateCartRequest, @PathVariable cartItemOptionId: Long)
    = cartService.updateCartItemCount(request, cartItemOptionId)

    @GetMapping
    fun getCartItems(@PageableDefault pageable: Pageable) = cartService.getCartItems(pageable)

    // TODO 삭제
}