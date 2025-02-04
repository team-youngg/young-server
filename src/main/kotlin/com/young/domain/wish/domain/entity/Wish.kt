package com.young.domain.wish.domain.entity

import com.young.domain.item.domain.entity.Item
import com.young.domain.user.domain.entity.User
import com.young.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "wish")
class Wish (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    val item: Item
) : BaseEntity()