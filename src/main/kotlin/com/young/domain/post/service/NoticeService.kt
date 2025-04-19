package com.young.domain.post.service

import com.young.domain.post.dto.request.CreateNoticeRequest
import com.young.domain.post.dto.request.UpdateNoticeRequest
import com.young.domain.post.dto.response.NoticeResponse
import com.young.domain.post.error.PostError
import com.young.domain.post.model.Notice
import com.young.domain.post.repository.NoticeRepository
import com.young.global.exception.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository
) {
    @Transactional
    fun createNotice(request: CreateNoticeRequest) {
        val notice = Notice(
            title = request.title,
            content = request.content
        )
        noticeRepository.save(notice)
    }

    @Transactional(readOnly = true)
    fun getNotice(id: Long): NoticeResponse {
        val notice = noticeRepository.findByIdOrNull(id) ?: throw CustomException(PostError.NOTICE_NOT_FOUND)
        return NoticeResponse.of(notice)
    }

    @Transactional(readOnly = true)
    fun getAllNotices(): List<NoticeResponse> =
        noticeRepository.findAll().map { NoticeResponse.of(it) }

    @Transactional
    fun updateNotice(id: Long, request: UpdateNoticeRequest): NoticeResponse {
        val notice = noticeRepository.findByIdOrNull(id)
            ?: throw CustomException(PostError.NOTICE_NOT_FOUND)
        notice.title = request.title ?: notice.title
        notice.content = request.content ?: notice.content
        return NoticeResponse.of(notice)
    }

    @Transactional
    fun deleteNotice(id: Long) {
        val notice = noticeRepository.findByIdOrNull(id)
            ?: throw CustomException(PostError.NOTICE_NOT_FOUND)
        noticeRepository.delete(notice)
    }
}