package com.young.domain.wish.error

import com.young.global.exception.CustomError

enum class WishError(override val status: Int, override val message: String) : CustomError{
    WISH_NOT_FOUND(404, "위시 상품을 찾을 수 없습니다."),
}