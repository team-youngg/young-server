package com.young.domain.info.dto.request

data class UpdateOrderInfoRequest(
    val title: String?,
    val address: String?,
    val addressDetail: String?,
    val tel: String?,
    val postCode: String?,
    val receiver: String?,
)