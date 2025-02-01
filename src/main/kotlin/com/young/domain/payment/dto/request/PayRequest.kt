package com.young.domain.payment.dto.request

data class PayRequest (
    val orderId: String,
    val amount: String,
    val paymentKey: String,
)