package com.young.domain.item.domain.entity

import com.young.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "items")
class Item (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    var price: Long,

    var detail: String,
) : BaseEntity()