package com.young.domain.payment.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true) // 사용하지 않는 필드는 무시
data class PaymentResponse(
    @JsonProperty("mId")
    val merchantId: String,  // 가맹점 ID

    @JsonProperty("paymentKey")
    val paymentKey: String,  // 결제 키

    @JsonProperty("orderId")
    val orderId: String,  // 주문 ID

    @JsonProperty("orderName")
    val orderName: String,  // 주문명

    @JsonProperty("status")
    val status: String,  // 결제 상태 (DONE이면 성공)

    @JsonProperty("requestedAt")
    val requestedAt: String,  // 결제 요청 시각 (ISO-8601)

    @JsonProperty("approvedAt")
    val approvedAt: String?,  // 결제 승인 시각 (성공 시 존재, ISO-8601)

    @JsonProperty("totalAmount")
    val totalAmount: Long,  // 총 결제 금액

    @JsonProperty("method")
    val method: String?,  // 결제 수단 (간편결제, 신용카드 등)

    @JsonProperty("currency")
    val currency: String,  // 통화 (KRW)

    @JsonProperty("easyPay")
    val easyPay: EasyPay?,  // 간편결제 정보 (null 가능)

    @JsonProperty("failure")
    val failure: Failure?,  // 결제 실패 정보 (null 가능)

    @JsonProperty("receipt")
    val receipt: Receipt?  // 영수증 정보 (null 가능)
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class EasyPay(
    @JsonProperty("provider")
    val provider: String,  // 간편결제 제공자 (토스페이, 카카오페이 등)

    @JsonProperty("amount")
    val amount: Long,  // 간편결제로 결제된 금액

    @JsonProperty("discountAmount")
    val discountAmount: Long // 간편결제 할인 금액
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Failure(
    @JsonProperty("code")
    val code: String?,  // 실패 코드

    @JsonProperty("message")
    val message: String?  // 실패 메시지
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Receipt(
    @JsonProperty("url")
    val url: String // 결제 영수증 URL
)

data class PaymentCancelResponse(
    val status: String,        // 취소 결과 상태 (예: "CANCELED")
    val canceledAt: String?,   // 취소 완료 일시
    val cancelAmount: Long?,   // 취소 금액
    val failure: Failure?      // 취소 실패 시 에러 정보
)