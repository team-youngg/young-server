package com.young.domain.payment.domain.entity

import com.young.domain.user.domain.entity.User
import com.young.global.common.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "payments")
class Payment (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val orderId: String,

    val paymentKey: String,

    val totalPrice: Long,

    val payMethod: String,

    val paidAt: LocalDateTime,

    val bankCode: String,

    val bankName: String,

    val isPaid: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
) : BaseEntity()