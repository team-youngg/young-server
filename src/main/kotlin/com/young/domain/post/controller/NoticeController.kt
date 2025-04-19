package com.young.domain.post.controller

import com.young.domain.post.dto.request.CreateNoticeRequest
import com.young.domain.post.dto.request.UpdateNoticeRequest
import com.young.domain.post.service.NoticeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "공지", description = "공지 api")
@RestController
@RequestMapping("/notices")
class NoticeController(
    private val noticeService: NoticeService
) {
    @Operation(summary = "공지 작성", description = "공지를 작성합니다.")
    @PostMapping
    fun createNotice(@RequestBody request: CreateNoticeRequest) = noticeService.createNotice(request)

    @Operation(summary = "공지 전체 조회", description = "전체 공지를 조회합니다.")
    @GetMapping
    fun getAllNotices() = noticeService.getAllNotices()

    @Operation(summary = "공지 조회", description = "공지를 조회합니다.")
    @GetMapping("/{noticeId}")
    fun getNotice(@PathVariable noticeId: Long) = noticeService.getNotice(noticeId)

    @Operation(summary = "공지 수정", description = "공지를 수정합니다.")
    @PutMapping("/{noticeId}")
    fun updateNotice(@PathVariable noticeId: Long, @RequestBody request: UpdateNoticeRequest)
    = noticeService.updateNotice(noticeId, request)

    @Operation(summary = "공지 삭제", description = "공지를 삭제합니다.")
    @DeleteMapping("/{noticeId}")
    fun deleteNotice(@PathVariable noticeId: Long) = noticeService.deleteNotice(noticeId)
}