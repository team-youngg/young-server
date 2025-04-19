package com.young.domain.post.dto.response

import com.young.domain.post.model.Inquiry
import com.young.domain.user.dto.response.UserResponse
import java.time.LocalDateTime

data class InquiryResponse(
    val id: Long,
    val title: String,
    val content: String,
    val author: UserResponse,
    val isPublic: Boolean,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(inquiry: Inquiry) = InquiryResponse(
            id = inquiry.id!!,
            title = inquiry.title,
            content = inquiry.content,
            author = UserResponse.of(inquiry.author),
            isPublic = inquiry.isPublic,
            createdAt = inquiry.createdAt
        )
    }
}