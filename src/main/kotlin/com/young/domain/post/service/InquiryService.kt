package com.young.domain.post.service

import com.young.domain.post.dto.request.CreateInquiryCommentRequest
import com.young.domain.post.dto.request.CreateInquiryRequest
import com.young.domain.post.dto.request.UpdateInquiryCommentRequest
import com.young.domain.post.dto.request.UpdateInquiryRequest
import com.young.domain.post.dto.response.InquiryCommentResponse
import com.young.domain.post.dto.response.InquiryResponse
import com.young.domain.post.error.PostError
import com.young.domain.post.model.Inquiry
import com.young.domain.post.model.InquiryComment
import com.young.domain.post.repository.InquiryCommentRepository
import com.young.domain.post.repository.InquiryRepository
import com.young.domain.user.error.UserError
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InquiryService(
    private val inquiryRepository: InquiryRepository,
    private val commentRepository: InquiryCommentRepository,
    private val securityHolder: SecurityHolder
) {
    @Transactional
    fun createInquiry(request: CreateInquiryRequest) {
        val author = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND);
        val inquiry = Inquiry(
            title = request.title,
            content = request.content,
            author = author,
            isPublic = request.isPublic
        )
        inquiryRepository.save(inquiry)
    }

    @Transactional
    fun getInquiry(id: Long): InquiryResponse {
        val inquiry = inquiryRepository.findByIdOrNull(id) ?: throw CustomException(PostError.INQUIRY_NOT_FOUND)
        return InquiryResponse.of(inquiry)
    }

    @Transactional
    fun getAllInquiries(): List<InquiryResponse> {
        return inquiryRepository.findAll().map { InquiryResponse.of(it) }
    }

    @Transactional
    fun updateInquiry(id: Long, request: UpdateInquiryRequest): InquiryResponse {
        val inquiry = inquiryRepository.findByIdOrNull(id) ?: throw CustomException(PostError.INQUIRY_NOT_FOUND)

        inquiry.title = request.title ?: inquiry.title
        inquiry.content = request.content ?: inquiry.content
        inquiry.isPublic = request.isPublic ?: inquiry.isPublic

        return InquiryResponse.of(inquiry)
    }

    @Transactional
    fun deleteInquiry(id: Long) {
        val inquiry = inquiryRepository.findByIdOrNull(id) ?: throw CustomException(PostError.INQUIRY_NOT_FOUND)
        inquiryRepository.delete(inquiry)
    }

    @Transactional
    fun createComment(request: CreateInquiryCommentRequest) {
        val author = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val inquiry = inquiryRepository.findByIdOrNull(request.inquiryId) ?:
            throw CustomException(PostError.INQUIRY_NOT_FOUND)

        val comment = InquiryComment(
            content = request.content,
            author = author,
            inquiry = inquiry,
            isPublic = request.isPublic
        )
        commentRepository.save(comment)
    }

    @Transactional
    fun getCommentsByInquiry(inquiryId: Long): List<InquiryCommentResponse> {
        return commentRepository.findByInquiryId(inquiryId).map { InquiryCommentResponse.of(it) }
    }

    @Transactional
    fun updateComment(id: Long, request: UpdateInquiryCommentRequest): InquiryCommentResponse {
        val comment = commentRepository.findByIdOrNull(id) ?: throw CustomException(PostError.COMMENT_NOT_FOUND)

        comment.content = request.content ?: comment.content
        comment.isPublic = request.isPublic ?: comment.isPublic

        return InquiryCommentResponse.of(comment)
    }

    @Transactional
    fun deleteComment(id: Long) {
        val comment = commentRepository.findByIdOrNull(id) ?: throw CustomException(PostError.COMMENT_NOT_FOUND)
        commentRepository.delete(comment)
    }
}