package com.young.domain.info.dto.response

import com.young.domain.info.domain.entity.UserOrderInfo

data class OrderInfoResponse(
    val id: Long,
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
                id = orderInfo.id!!,
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
