package com.young.domain.item.repository

import com.young.domain.item.domain.entity.ItemOption
import org.springframework.data.jpa.repository.JpaRepository

interface ItemOptionRepository : JpaRepository<ItemOption, Long> {
}