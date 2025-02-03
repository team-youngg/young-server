package com.young.domain.item.repository

import com.young.domain.item.domain.entity.Item
import com.young.domain.item.domain.entity.ItemImage
import org.springframework.data.jpa.repository.JpaRepository

interface ItemImageRepository : JpaRepository<ItemImage, Long> {
    fun findAllByItem(item: Item): List<ItemImage>
}