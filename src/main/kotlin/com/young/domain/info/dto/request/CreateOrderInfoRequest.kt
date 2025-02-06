package com.young.domain.info.dto.request

data class CreateOrderInfoRequest(
    val isDefault: Boolean,
    val title: String,
    val address: String,
    val addressDetail: String,
    val tel: String,
    val postCode: String,
    val receiver: String,
)