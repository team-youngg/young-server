package com.young.domain.review.error

import com.young.global.exception.CustomError

enum class ReviewError(override val status: Int, override val message: String) : CustomError {
    REVIEW_NOT_FOUND(404, "리뷰를 찾을 수 없습니다."),
}