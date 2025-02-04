package com.young.domain.order.domain.entity

import com.young.domain.item.domain.entity.ItemOption
import com.young.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "order_item_options")
class OrderItemOption (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    val orderItem: OrderItem,

    @Column(name = "count", nullable = false)
    var count: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id")
    var itemOption: ItemOption,
) : BaseEntity()