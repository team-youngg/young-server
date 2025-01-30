package com.young.domain.item.repository

import com.young.domain.item.domain.entity.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {
    fun findAllByOrderByCreatedAtDesc(pageable: Pageable): Page<Item>
}