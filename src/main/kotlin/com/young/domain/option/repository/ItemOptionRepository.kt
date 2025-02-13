package com.young.domain.option.repository

import com.young.domain.item.domain.entity.Item
import com.young.domain.option.domain.entity.ItemOption
import com.young.domain.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ItemOptionRepository : JpaRepository<ItemOption, Long> {
    fun findAllByItem(item: Item): List<ItemOption>
}