package com.young.domain.payment.repository

import com.young.domain.payment.domain.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PaymentRepository : JpaRepository<Payment, Long> {
    fun findByPaymentKey(paymentKey: String): Payment?
    fun findByOrderId(orderId: String): Payment?
}