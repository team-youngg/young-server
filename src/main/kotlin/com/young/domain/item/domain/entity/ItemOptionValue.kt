package com.young.domain.item.domain.entity

import com.young.domain.item.domain.enums.ItemOptionType
import com.young.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "item_option_values")
class ItemOptionValue (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val type: ItemOptionType,

    @Column(nullable = false)
    val value: String,

    @Column(nullable = false)
    val detail: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id", nullable = false)
    val itemOption: ItemOption,
) : BaseEntity()