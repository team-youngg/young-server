package com.young.domain.cart.error

import com.young.global.exception.CustomError

enum class CartError(override val status: Int, override val message: String) : CustomError {
    CART_ALREADY_EXISTS(400, "한 유저 당 하나의 카트만 존재합니다."),
    CART_NOT_FOUND(404, "카트를 찾을 수 없습니다."),
    CART_ITEM_DUPLICATED(400, "이미 카트에 존재하는 상품입니다."),
    CART_ITEM_NOT_FOUND(404, "카트에서 상품을 찾을 수 없습니다.")
}