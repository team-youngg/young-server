package com.young.domain.order.domain.entity

import com.young.domain.order.domain.enums.OrderStatus
import com.young.domain.payment.domain.entity.Payment
import com.young.domain.user.domain.entity.User
import com.young.global.common.BaseEntity
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "orders")
class Order (
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: OrderStatus,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = true)
    var payment: Payment? = null,
) : BaseEntity()