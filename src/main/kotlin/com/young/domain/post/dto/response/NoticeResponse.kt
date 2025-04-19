package com.young.domain.post.dto.response

import com.young.domain.post.model.Notice
import java.time.LocalDateTime

data class NoticeResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(notice: Notice) = NoticeResponse(
            id = notice.id!!,
            title = notice.title,
            content = notice.content,
            createdAt = notice.createdAt
        )
    }
}