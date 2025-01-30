package com.young.domain.item.repository

import com.young.domain.item.domain.entity.Item
import com.young.domain.item.domain.entity.ItemOption
import org.springframework.data.jpa.repository.JpaRepository

interface ItemOptionRepository : JpaRepository<ItemOption, Long> {
    fun existsByItemAndName(item: Item, name: String): Boolean
    fun findAllByItem(item: Item): List<ItemOption>
}