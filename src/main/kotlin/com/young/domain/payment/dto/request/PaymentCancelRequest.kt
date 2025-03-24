package com.young.domain.payment.dto.request

import java.util.UUID

data class PaymentCancelRequest(
    val orderId: UUID,
    val paymentKey: String,
    val cancelReason: String // 취소 사유
)