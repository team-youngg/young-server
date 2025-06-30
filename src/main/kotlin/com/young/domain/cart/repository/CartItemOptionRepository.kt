package com.young.domain.cart.repository

import com.young.domain.cart.domain.entity.Cart
import com.young.domain.cart.domain.entity.CartItem
import com.young.domain.cart.domain.entity.CartItemOption
import com.young.domain.option.domain.entity.ItemOption
import org.springframework.data.jpa.repository.JpaRepository

interface CartItemOptionRepository : JpaRepository<CartItemOption, Long> {
    fun findByCartItem(cartItem: CartItem): List<CartItemOption>
    fun existsByItemOption(itemOption: ItemOption): Boolean
    fun findByItemOption(itemOption: ItemOption): CartItemOption?
    fun existsByItemOptionAndCartItem_Cart(itemOption: ItemOption, cart: Cart): Boolean
}