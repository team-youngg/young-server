package com.young.domain.payment.error

import com.young.global.exception.CustomError

enum class PaymentError(override val status: Int, override val message: String) : CustomError {
    API_ERROR(500, "결제 확인 중 에러가 발생했습니다. (에러: %s)"),
}