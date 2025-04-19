package com.young.domain.post.dto.response

import com.young.domain.post.model.InquiryComment
import com.young.domain.user.dto.response.UserResponse
import java.time.LocalDateTime

data class InquiryCommentResponse(
    val id: Long,
    val content: String,
    val author: UserResponse,
    val isPublic: Boolean,
    val inquiryId: Long,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(comment: InquiryComment) = InquiryCommentResponse(
            id = comment.id!!,
            content = comment.content,
            author = UserResponse.of(comment.author),
            isPublic = comment.isPublic,
            inquiryId = comment.inquiry.id!!,
            createdAt = comment.createdAt
        )
    }
}