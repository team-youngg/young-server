package com.young.domain.cart.service

import com.young.domain.cart.domain.entity.CartItem
import com.young.domain.cart.domain.entity.CartItemOption
import com.young.domain.cart.dto.request.CreateCartRequest
import com.young.domain.cart.dto.request.UpdateCartRequest
import com.young.domain.cart.dto.response.CartItemResponse
import com.young.domain.cart.error.CartError
import com.young.domain.cart.repository.CartItemOptionRepository
import com.young.domain.cart.repository.CartItemRepository
import com.young.domain.cart.repository.CartRepository
import com.young.domain.item.dto.response.ItemResponse
import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.ItemOptionRepository
import com.young.domain.item.repository.ItemOptionValueRepository
import com.young.domain.item.repository.ItemRepository
import com.young.domain.item.util.ItemUtil
import com.young.domain.user.error.UserError
import com.young.domain.wish.repository.WishRepository
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
    private val itemOptionRepository: ItemOptionRepository,
    private val cartItemOptionRepository: CartItemOptionRepository,
    private val itemOptionValueRepository: ItemOptionValueRepository,
    private val itemUtil: ItemUtil,
    private val wishRepository: WishRepository,
) {
    @Transactional
    fun createCartItem(request: CreateCartRequest) {
        // TODO 이미 있는 아이템 && 옵션이면 재고추가
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val cart = cartRepository.findByUser(user) ?: throw CustomException(CartError.CART_NOT_FOUND)
        val itemOption = itemOptionRepository.findByIdOrNull(request.optionId)
            ?: throw CustomException(ItemError.OPTION_NOT_FOUND)
        if (itemOption.stock == 0L) throw CustomException(ItemError.NO_STOCK)
        if (cartItemOptionRepository.existsByItemOption(itemOption)) throw CustomException(CartError.CART_ITEM_DUPLICATED)

        val cartItem = CartItem(
            cart = cart,
            item = itemOption.item,
        )
        cartItemRepository.save(cartItem)

        val cartItemOption = CartItemOption(
            itemOption = itemOption,
            cartItem = cartItem,
            count = 1
        )
        cartItemOptionRepository.save(cartItemOption)
    }

    @Transactional
    fun updateCartItemCount(request: UpdateCartRequest, cartItemIdOption: Long) {
        val cartItemOption = cartItemOptionRepository.findByIdOrNull(cartItemIdOption)
            ?: throw CustomException(ItemError.OPTION_NOT_FOUND)

        if (request.isPlus) cartItemOption.count += request.count
        else cartItemOption.count -= request.count

        cartItemOptionRepository.save(cartItemOption)
    }

    @Transactional
    fun getCartItems(pageable: Pageable): List<CartItemResponse> {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val cart = cartRepository.findByUser(user) ?: throw CustomException(CartError.CART_NOT_FOUND)
        val cartItems = cartItemRepository.findAllByCart(cart, pageable)

        return cartItems.map { cartItem ->
            val cartItemOptions = cartItemOptionRepository.findByCartItem(cartItem)
            val itemOptionValues = cartItemOptions.map { cartItemOption ->
                val itemOptionValue = itemOptionValueRepository.findAllByItemOption(cartItemOption.itemOption)
                val item = cartItemOption.cartItem.item
                val itemElements = itemUtil.getItemElements(item)
                val wishItemIds: Set<Long> = if (user != null) {
                    wishRepository.findItemIdsByUser(user).toSet()
                } else {
                    emptySet()
                }
                val itemResponse = ItemResponse.of(
                    item,
                    itemElements.images,
                    itemElements.options,
                    itemElements.optionValues,
                    itemElements.categories,
                    isWish = item.id in wishItemIds
                )
                CartItemResponse.of(cartItemOption, itemOptionValue, itemResponse)
            }
            itemOptionValues
        }.flatten()
    }
}