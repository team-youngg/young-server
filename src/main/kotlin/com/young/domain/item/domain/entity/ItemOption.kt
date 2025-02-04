package com.young.domain.item.domain.entity

import com.young.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "item_options")
class ItemOption (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    val item: Item,

    @Column(nullable = false)
    var stock: Long,
) : BaseEntity()