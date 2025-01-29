package com.young.domain.item.repository

import com.young.domain.item.domain.entity.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {
}