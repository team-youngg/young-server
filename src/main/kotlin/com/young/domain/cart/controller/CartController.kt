package com.young.domain.cart.controller

import com.young.domain.cart.dto.request.CreateCartRequest
import com.young.domain.cart.dto.request.UpdateCartRequest
import com.young.domain.cart.service.CartService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "카트", description = "카트(장바구니) api")
@RestController
@RequestMapping("/carts")
class CartController (
    private val cartService: CartService
) {
    @Operation(summary = "카트 담기", description = "카트에 상품을 추가합니다.")
    @PostMapping
    fun createCartItem(@RequestBody request: CreateCartRequest) = cartService.createCartItem(request)

    @Operation(summary = "카트 상품 갯수 수정", description = "상품 갯수를 수정합니다.")
    @PatchMapping("/{cartItemOptionId}")
    fun updateCartItem(@RequestBody request: UpdateCartRequest, @PathVariable cartItemOptionId: Long)
    = cartService.updateCartItemCount(request, cartItemOptionId)

    @Operation(summary = "카트 조회", description = "카트의 상품들을 조회합니다.")
    @GetMapping
    fun getCartItems(@PageableDefault pageable: Pageable) = cartService.getCartItems(pageable)

    // TODO 삭제
}