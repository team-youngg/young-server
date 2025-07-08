package com.young.domain.item.repository

import com.young.domain.item.domain.entity.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {
    fun findAllByPurchasableIsTrueOrderByCreatedAtDesc(pageable: Pageable): Page<Item>
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<Item>
    fun findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndPurchasableIsTrue(
        name: String, description: String, pageable: Pageable
    ): Page<Item>
    fun findByPriceBetweenAndPurchasableIsTrue(minPrice: Long, maxPrice: Long, pageable: Pageable): Page<Item>
    fun findByPriceGreaterThanEqualAndPurchasableIsTrue(minPrice: Long, pageable: Pageable): Page<Item>
    fun findByPriceLessThanEqualAndPurchasableIsTrue(maxPrice: Long, pageable: Pageable): Page<Item>
}