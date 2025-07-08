package com.young.domain.image.domain.entity

import com.young.domain.item.domain.entity.Item
import jakarta.persistence.*

@Entity
@Table(name = "item_images")
class ItemImage (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    val url: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    val item: Item? = null,
)