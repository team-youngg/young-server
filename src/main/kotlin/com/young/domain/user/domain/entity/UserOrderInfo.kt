package com.young.domain.user.domain.entity

import com.young.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "user_order_infos")
class UserOrderInfo (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var title: String,

    var address: String,

    var addressDetail: String,

    var tel: String,

    var postCode: String,

    var receiver: String,

    var isDefault: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val user: User
) : BaseEntity()