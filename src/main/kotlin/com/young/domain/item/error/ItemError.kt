package com.young.domain.item.error

import com.young.global.exception.CustomError

enum class ItemError(override val status: Int, override val message: String) : CustomError {
    ITEM_NOT_FOUND(404, "찾을 수 없는 상품입니다."),
    OPTION_NOT_FOUND(404, "찾을 수 없는 옵션입니다.")
}