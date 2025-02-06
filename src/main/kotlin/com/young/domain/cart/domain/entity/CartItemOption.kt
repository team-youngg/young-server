package com.young.domain.cart.domain.entity

import com.young.domain.option.domain.entity.ItemOption
import com.young.global.common.BaseEntity
import jakarta.persistence.*

@Entity
class CartItemOption (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, name = "count")
    var count: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id")
    val cartItem: CartItem,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id")
    var itemOption: ItemOption,
) : BaseEntity()