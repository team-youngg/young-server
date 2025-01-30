package com.young.domain.item.domain.entity

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

    @Column(nullable = false)
    var stock: Long,
)