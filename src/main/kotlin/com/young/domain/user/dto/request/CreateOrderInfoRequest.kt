package com.young.domain.user.dto.request

data class CreateOrderInfoRequest(
    val isDefault: Boolean,
    val title: String,
    val address: String,
    val addressDetail: String,
    val tel: String,
    val postCode: String,
    val receiver: String,
)