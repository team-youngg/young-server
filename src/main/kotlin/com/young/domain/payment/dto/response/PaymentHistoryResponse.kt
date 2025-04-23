package com.young.domain.payment.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.young.domain.payment.domain.entity.Payment

@JsonIgnoreProperties(ignoreUnknown = true) // 사용하지 않는 필드는 무시
data class PaymentHistoryResponse(
    @JsonProperty("paymentKey")
    val paymentKey: String,  // 결제 키

    @JsonProperty("orderId")
    val orderId: String,  // 주문 ID

    @JsonProperty("requestedAt")
    val requestedAt: String,  // 결제 요청 시각 (ISO-8601)

    @JsonProperty("approvedAt")
    val approvedAt: String?,  // 결제 승인 시각 (성공 시 존재, ISO-8601)

    @JsonProperty("totalAmount")
    val totalAmount: Long,  // 총 결제 금액

    @JsonProperty("method")
    val method: String?,  // 결제 수단 (간편결제, 신용카드 등)
) {
    companion object {
        fun of(payment: Payment): PaymentHistoryResponse {
            return PaymentHistoryResponse(
                paymentKey = payment.paymentKey,
                orderId = payment.orderId,
                requestedAt = payment.createdAt.toString(),
                approvedAt = payment.modifiedAt.toString(),
                totalAmount = payment.totalPrice,
                method = payment.payMethod
            )
        }
    }
}