package com.young.domain.item.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "item_categories")
class ItemCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "item_id")
    val item: Item,

    val categoryId: Long
)