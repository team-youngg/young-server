package com.young.domain.image.error

import com.young.global.exception.CustomError

enum class ImageError(override val status: Int, override val message: String) : CustomError {
    IMAGE_NOT_FOUND(404, "찾을 수 없는 이미지입니다."),
}