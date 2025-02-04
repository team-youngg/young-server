package com.young.domain.payment.repository

import com.young.domain.payment.domain.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<Payment, Long> {
}