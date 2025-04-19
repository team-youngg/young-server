package com.young.domain.post.dto.request

data class UpdateInquiryCommentRequest(
    val content: String? = null,
    val isPublic: Boolean? = null,
)