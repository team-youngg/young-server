package com.young.domain.post.controller

import com.young.domain.post.dto.request.CreateNoticeRequest
import com.young.domain.post.dto.request.UpdateNoticeRequest
import com.young.domain.post.service.NoticeService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/notices")
class NoticeController(
    private val noticeService: NoticeService
) {
    @PostMapping
    fun createNotice(@RequestBody request: CreateNoticeRequest) = noticeService.createNotice(request)

    @GetMapping
    fun getAllNotices() = noticeService.getAllNotices()

    @GetMapping("/{noticeId}")
    fun getNotice(@PathVariable noticeId: Long) = noticeService.getNotice(noticeId)

    @PutMapping("/{noticeId}")
    fun updateNotice(@PathVariable noticeId: Long, @RequestBody request: UpdateNoticeRequest)
    = noticeService.updateNotice(noticeId, request)

    @DeleteMapping("/{noticeId}")
    fun deleteNotice(@PathVariable noticeId: Long) = noticeService.deleteNotice(noticeId)
}