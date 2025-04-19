package com.young.domain.post.controller

import com.young.domain.post.dto.request.CreateInquiryCommentRequest
import com.young.domain.post.dto.request.CreateInquiryRequest
import com.young.domain.post.dto.request.UpdateInquiryCommentRequest
import com.young.domain.post.dto.request.UpdateInquiryRequest
import com.young.domain.post.service.InquiryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "문의", description = "문의 api")
@RestController
@RequestMapping("/inquiries")
class InquiryController(
    private val inquiryService: InquiryService
) {
    @Operation(summary = "문의 작성", description = "문의를 작성합니다.")
    @PostMapping
    fun createInquiry(@RequestBody request: CreateInquiryRequest) = inquiryService.createInquiry(request)

    @Operation(summary = "문의 전체 조회", description = "전체 문의를 조회합니다.")
    @GetMapping
    fun getAllInquiries() = inquiryService.getAllInquiries()

    @Operation(summary = "문의 조회", description = "문의를 조회합니다.")
    @GetMapping("/{inquiryId}")
    fun getInquiry(@PathVariable inquiryId: Long) = inquiryService.getInquiry(inquiryId)

    @Operation(summary = "문의 수정", description = "문의를 수정합니다.")
    @PutMapping("/{inquiryId}")
    fun updateInquiry(@PathVariable inquiryId: Long, @RequestBody request: UpdateInquiryRequest)
    = inquiryService.updateInquiry(inquiryId, request)

    @Operation(summary = "문의 삭제", description = "문의를 삭제합니다.")
    @DeleteMapping("/{inquiryId}")
    fun deleteInquiry(@PathVariable inquiryId: Long) = inquiryService.deleteInquiry(inquiryId)

    @Operation(summary = "문의 답변 작성", description = "문의 답변을 작성합니다.")
    @PostMapping("/comments")
    fun createComment(@RequestBody request: CreateInquiryCommentRequest) = inquiryService.createComment(request)

    @Operation(summary = "문의 답변 전체 조회", description = "문의 답변을 전체 조회합니다.")
    @GetMapping("/comments/{inquiryId}")
    fun getComments(@PathVariable inquiryId: Long) = inquiryService.getCommentsByInquiry(inquiryId)

    @Operation(summary = "문의 답변 수정", description = "문의 답변을 수정합니다.")
    @PutMapping("/comments/{commentId}")
    fun updateComment(@PathVariable commentId: Long, @RequestBody request: UpdateInquiryCommentRequest)
    = inquiryService.updateComment(commentId, request)

    @Operation(summary = "문의 답변 삭제", description = "문의 답변을 삭제합니다.")
    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(@PathVariable commentId: Long) = inquiryService.deleteComment(commentId)
}