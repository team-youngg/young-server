package com.young.domain.cart.repository

import com.young.domain.cart.domain.entity.Cart
import com.young.domain.cart.domain.entity.CartItem
import com.young.domain.item.domain.entity.Item
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CartItemRepository : JpaRepository<CartItem, Long> {
    fun findByCartAndItem(cart: Cart, item: Item): CartItem?
    fun existsByCartAndItem(cart: Cart, item: Item): Boolean
    fun findAllByCart(cart: Cart, pageable: Pageable): List<CartItem>
}