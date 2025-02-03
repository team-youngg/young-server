package com.young.domain.item.domain.entity

import jakarta.persistence.*

@Entity
class ItemColor (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val color: String,

    @Column(nullable = false)
    val hex: String,
)