package com.young.domain.post.dto.request

data class CreateInquiryRequest(
    val title: String,
    val content: String,
    val isPublic: Boolean,
)