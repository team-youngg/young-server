package com.young.domain.cart.domain.entity

import com.young.domain.item.domain.entity.Item
import com.young.global.common.BaseEntity
import jakarta.persistence.*

@Entity
class CartItem (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    val item: Item,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    val cart: Cart,

    @Column(nullable = false)
    var amount: Long,
) : BaseEntity()