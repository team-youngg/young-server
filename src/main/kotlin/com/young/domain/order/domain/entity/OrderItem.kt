package com.young.domain.order.domain.entity

import com.young.domain.item.domain.entity.Item
import com.young.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "order_items")
class OrderItem (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    @Column(name = "count", nullable = false)
    var count: Long,

    @Column(name = "option", nullable = false)
    var option: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    val item: Item
) : BaseEntity()