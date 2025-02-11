package com.young.domain.item.service

import com.young.domain.item.dto.response.ItemResponse
import com.young.domain.item.repository.ItemRepository
import com.young.domain.item.util.ItemUtil
import com.young.global.security.SecurityHolder
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ItemSearchService (
    private val securityHolder: SecurityHolder,
    private val itemRepository: ItemRepository,
    private val itemUtil: ItemUtil
) {
    @Transactional(readOnly = true)
    fun searchItems(query: String, pageable: Pageable): List<ItemResponse> {
        val user = securityHolder.user
        val items = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, pageable)
        return items.map { itemUtil.toItemResponse(it, user) }
    }

    @Transactional(readOnly = true)
    fun searchItemsByPrice(minPrice: Long?, maxPrice: Long?, pageable: Pageable): List<ItemResponse> {
        val user = securityHolder.user
        val items = when {
            minPrice != null && maxPrice != null -> itemRepository.findByPriceBetween(minPrice, maxPrice, pageable)
            minPrice != null -> itemRepository.findByPriceGreaterThanEqual(minPrice, pageable)
            maxPrice != null -> itemRepository.findByPriceLessThanEqual(maxPrice, pageable)
            else -> itemRepository.findAll()
        }
        return items.map { itemUtil.toItemResponse(it, user) }
    }
}