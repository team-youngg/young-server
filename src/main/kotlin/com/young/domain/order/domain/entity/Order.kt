package com.young.domain.order.domain.entity

import com.young.domain.order.domain.enums.OrderStatus
import com.young.domain.payment.domain.entity.Payment
import com.young.domain.user.domain.entity.User
import com.young.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "orders")
class Order (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: OrderStatus,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    var payment: Payment? = null,
) : BaseEntity()