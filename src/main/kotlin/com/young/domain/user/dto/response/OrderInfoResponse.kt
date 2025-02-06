package com.young.domain.user.dto.response

import com.young.domain.user.domain.entity.UserOrderInfo

data class OrderInfoResponse(
    val isDefault: Boolean,
    val title: String,
    val address: String,
    val addressDetail: String,
    val tel: String,
    val postCode: String,
    val receiver: String,
) {
    companion object {
        fun of(orderInfo: UserOrderInfo): OrderInfoResponse {
            return OrderInfoResponse(
                isDefault = orderInfo.isDefault,
                title = orderInfo.title,
                address = orderInfo.address,
                addressDetail = orderInfo.addressDetail,
                tel = orderInfo.tel,
                postCode = orderInfo.postCode,
                receiver = orderInfo.receiver,
            )
        }
    }
}
