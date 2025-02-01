package com.young.domain.payment.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class PaymentResponse(
    val paymentKey: String,
    val orderId: String,
    val status: String?,
    val method: String?,
    val totalAmount: Int?,
//    val receipt: String?
)