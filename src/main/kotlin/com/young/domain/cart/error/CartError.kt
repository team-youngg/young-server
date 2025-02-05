package com.young.domain.cart.error

import com.young.global.exception.CustomError

enum class CartError(override val status: Int, override val message: String) : CustomError {
    CART_NOT_FOUND(404, "카트를 찾을 수 없습니다."),
    CART_ITEM_DUPLICATED(400, "이미 카트에 존재하는 상품의 옵션입니다."),
    COUNT_UNDER_ZERO(400, "상품 개수가 0 이하입니다.")
}