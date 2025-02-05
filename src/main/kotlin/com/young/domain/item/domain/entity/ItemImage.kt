package com.young.domain.item.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "item_images")
class ItemImage (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val url: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    val item: Item? = null,
)