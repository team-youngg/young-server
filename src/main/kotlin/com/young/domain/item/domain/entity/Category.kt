package com.young.domain.item.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "categories")
class Category(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,

    val parentId: Long?
)