package com.young.domain.option.repository

import com.young.domain.option.domain.entity.ItemOption
import com.young.domain.option.domain.entity.ItemOptionValue
import com.young.domain.option.domain.enums.ItemOptionType
import org.springframework.data.jpa.repository.JpaRepository

interface ItemOptionValueRepository : JpaRepository<ItemOptionValue, Long> {
    fun findAllByItemOption(option: ItemOption): List<ItemOptionValue>
    fun findByTypeAndValue(type: ItemOptionType, value: String): ItemOptionValue?
}