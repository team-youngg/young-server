package com.young.domain.item.domain.entity

import com.young.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "items")
class Item (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var description: String,

    @Column(nullable = false)
    var price: Long,

    var detail: String,

    var gender: String
) : BaseEntity()