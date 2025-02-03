package com.young.domain.item.repository

import com.young.domain.item.domain.entity.ItemColor
import org.springframework.data.jpa.repository.JpaRepository

interface ItemColorRepository : JpaRepository<ItemColor, Long> {
    fun existsByColorAndHex(color: String, hex: String): Boolean
    fun findByColorAndHex(color: String, hex: String): ItemColor?
}