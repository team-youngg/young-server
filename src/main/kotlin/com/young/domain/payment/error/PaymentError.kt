package com.young.domain.payment.error

import com.young.global.exception.CustomError

enum class PaymentError(override val status: Int, override val message: String) : CustomError {
    API_ERROR(500, "결제 확인 중 에러가 발생했습니다. (에러: %s)"),
    PAYMENT_FAILED(500, "결제 승인에 실패했습니다. (status: %s)"),
    PAYMENT_NOT_FOUND(404, "찾을 수 없는 결제입니다."),
    NOT_PAID(400, "승인되지 않은 결제입니다."),
    ALREADY_PAID(400, "이미 결제된 주문입니다."),
    PAYMENT_CANCELLATION_FAILED(500, "결제 취소에 실패하였습니다."),
    PAYMENT_NOT_PAID(400, "완료하지 않은 결제는 취소할 수 없습니다."),
    ALREADY_CHECKED(400, "이미 확인된 주문은 취소할 수 없습니다."),
}