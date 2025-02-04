package com.young.domain.item.repository

import com.young.domain.item.domain.entity.ItemOption
import com.young.domain.item.domain.entity.ItemOptionValue
import com.young.domain.item.domain.enums.ItemOptionType
import org.springframework.data.jpa.repository.JpaRepository

interface ItemOptionValueRepository : JpaRepository<ItemOptionValue, Int> {
    fun findAllByItemOption(option: ItemOption): List<ItemOptionValue>
    fun findByTypeAndValue(type: ItemOptionType, value: String): ItemOptionValue?
}