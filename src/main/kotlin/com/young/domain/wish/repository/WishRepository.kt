package com.young.domain.wish.repository

import com.young.domain.item.domain.entity.Item
import com.young.domain.user.domain.entity.User
import com.young.domain.wish.domain.entity.Wish
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface WishRepository : JpaRepository<Wish, Long> {
    fun findByUserAndItem(user: User, item: Item): Wish?
    fun findByUser(user: User): List<Wish>
    @Query("SELECT w.item.id FROM Wish w WHERE w.user = :user")
    fun findItemIdsByUser(@Param("user") user: User): List<Long>
}