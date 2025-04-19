package com.young.domain.post.dto.request

data class UpdateInquiryRequest(
    val title: String? = null,
    val content: String? = null,
    val isPublic: Boolean? = null,
)