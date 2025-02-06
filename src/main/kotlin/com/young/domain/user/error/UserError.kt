package com.young.domain.user.error

import com.young.global.exception.CustomError

enum class UserError(override val status: Int, override val message: String) : CustomError {
    USER_NOT_FOUND(404, "유저를 찾을 수 없습니다."),
    INFO_NOT_FOUND(404, "유저 배송지 정보를 찾을 수 없습니다.")
}