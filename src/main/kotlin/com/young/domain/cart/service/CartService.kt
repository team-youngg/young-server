package com.young.domain.cart.service

import com.young.domain.cart.domain.entity.CartItem
import com.young.domain.cart.dto.request.CreateCartRequest
import com.young.domain.cart.dto.request.UpdateCartRequest
import com.young.domain.cart.dto.response.CartItemResponse
import com.young.domain.cart.error.CartError
import com.young.domain.cart.repository.CartItemRepository
import com.young.domain.cart.repository.CartRepository
import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.ItemRepository
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartService (
    private val cartRepository: CartRepository,
    private val securityHolder: SecurityHolder,
    private val cartItemRepository: CartItemRepository,
    private val itemRepository: ItemRepository,
) {
    @Transactional
    fun createCartItem(request: CreateCartRequest) {
        // TODO 수량제한
        val user = securityHolder.user
        val cart = cartRepository.findByUser(user) ?: throw CustomException(CartError.CART_NOT_FOUND)
        val item = itemRepository.findByIdOrNull(request.itemId) ?: throw CustomException(ItemError.ITEM_NOT_FOUND)

        if (cartItemRepository.existsByCartAndItem(cart, item)) throw CustomException(CartError.CART_ITEM_DUPLICATED)

        val cartItem = CartItem(
            cart = cart,
            item = item,
            amount = request.amount
        )
        cartItemRepository.save(cartItem)
    }

    @Transactional
    fun updateCartItem(request: UpdateCartRequest) {
        val cartItem = cartItemRepository.findByIdOrNull(request.cartItemId)
            ?: throw CustomException(CartError.CART_ITEM_NOT_FOUND)

        cartItem.amount = request.amount ?: cartItem.amount
        cartItemRepository.save(cartItem)
    }

    @Transactional
    fun getCartItems(pageable: Pageable): List<CartItemResponse> {
        val user = securityHolder.user
        val cart = cartRepository.findByUser(user) ?: throw CustomException(CartError.CART_NOT_FOUND)
        val cartItems = cartItemRepository.findAllByCart(cart, pageable)

        return cartItems.map { cartItem -> CartItemResponse.of(cartItem) }
    }
}