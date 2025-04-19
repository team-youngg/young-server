package com.young.domain.post.repository

import com.young.domain.post.model.InquiryComment
import org.springframework.data.jpa.repository.JpaRepository

interface InquiryCommentRepository : JpaRepository<InquiryComment, Long> {
    fun findByInquiryId(id: Long): List<InquiryComment>
}