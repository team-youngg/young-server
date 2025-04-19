package com.young.domain.post.dto.request

data class CreateInquiryCommentRequest(
    val inquiryId: Long,
    val content: String,
    val isPublic: Boolean
)
