package com.young.domain.payment.dto.request

import java.util.UUID

data class PayRequest (
    val orderId: UUID,
    val amount: String,
    val paymentKey: String,
)