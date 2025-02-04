package com.young.domain.item.error

import com.young.global.exception.CustomError

enum class ItemError(override val status: Int, override val message: String) : CustomError {
    ITEM_NOT_FOUND(404, "찾을 수 없는 상품입니다."),
    OPTION_NOT_FOUND(404, "찾을 수 없는 옵션입니다."),
    CATEGORY_NOT_FOUND(404, "찾을 수 없는 카테고리입니다."),
    NO_STOCK(400, "재고가 없는 옵션입니다."),
    ITEM_OPTION_NOT_MATCH(400, "해당 아이템의 옵션이 아닙니다."),
}