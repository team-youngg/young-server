package com.young.domain.item.repository

import com.young.domain.item.domain.entity.ItemCategory
import org.springframework.data.jpa.repository.JpaRepository

interface ItemCategoryRepository : JpaRepository<ItemCategory, Long> {
}