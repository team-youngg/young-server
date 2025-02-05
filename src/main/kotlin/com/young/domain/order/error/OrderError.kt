package com.young.domain.order.error

import com.young.global.exception.CustomError

enum class OrderError(override val status: Int, override val message: String) : CustomError {
    ORDER_NOT_FOUND(404, "찾을 수 없는 주문입니다."),
}