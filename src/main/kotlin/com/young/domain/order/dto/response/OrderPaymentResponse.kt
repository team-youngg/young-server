package com.young.domain.order.dto.response

import com.young.domain.payment.dto.response.PaymentHistoryResponse

data class OrderPaymentResponse(
    val payment: PaymentHistoryResponse,
    val order: OrderResponse
)
