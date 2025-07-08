package com.young.domain.option.repository

import com.young.domain.item.domain.entity.Item
import com.young.domain.option.domain.entity.ItemOption
import com.young.domain.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ItemOptionRepository : JpaRepository<ItemOption, Long> {
    fun findAllByItem(item: Item): List<ItemOption>

    @Modifying
    @Query("""
        UPDATE ItemOption io
           SET io.stock = io.stock - :cnt
         WHERE io.id    = :id
           AND io.stock >= :cnt
    """)
    fun decreaseStock(id: Long, cnt: Long): Int
    fun deleteAllByItem(item: Item)
}