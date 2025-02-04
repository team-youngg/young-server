package com.young.domain.wish.repository

import com.young.domain.item.domain.entity.Item
import com.young.domain.user.domain.entity.User
import com.young.domain.wish.domain.entity.Wish
import org.springframework.data.jpa.repository.JpaRepository

interface WishRepository : JpaRepository<Wish, Long> {
    fun findByUserAndItem(user: User, item: Item): Wish?
    fun findByUser(user: User): List<Wish>
}