package com.young.domain.wish.service

import com.young.domain.item.dto.response.ItemResponse
import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.ItemRepository
import com.young.domain.item.util.ItemUtil
import com.young.domain.user.error.UserError
import com.young.domain.wish.domain.entity.Wish
import com.young.domain.wish.dto.request.WishRequest
import com.young.domain.wish.repository.WishRepository
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WishService (
    private val wishRepository: WishRepository,
    private val itemRepository: ItemRepository,
    private val securityHolder: SecurityHolder,
    private val itemUtil: ItemUtil
) {
    @Transactional
    fun updateWishItem(request: WishRequest) {
        val item = itemRepository.findByIdOrNull(request.itemId) ?: throw CustomException(ItemError.ITEM_NOT_FOUND)
        if (!item.purchasable) throw CustomException(ItemError.ITEM_NOT_FOUND) // 구매 불가 상품은 찜할 수 없음
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val wish = wishRepository.findByUserAndItem(user, item)

        if (wish != null) {
            wishRepository.delete(wish)
        } else {
            val newWish = Wish(user = user, item = item)
            wishRepository.save(newWish)
        }
    }

    @Transactional
    fun getWishList(): List<ItemResponse> {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val wish = wishRepository.findByUser(user)
        val items = wish.map { it.item }

        return items.map { item ->
            val itemElements = itemUtil.getItemElements(item)
            ItemResponse.of(
                item,
                itemElements.images,
                itemElements.options,
                itemElements.optionValues,
                itemElements.categories,
                isWish = true
            )
        }
    }
}