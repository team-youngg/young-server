package com.young.domain.post.controller

import com.young.domain.post.dto.request.CreateInquiryCommentRequest
import com.young.domain.post.dto.request.CreateInquiryRequest
import com.young.domain.post.dto.request.UpdateInquiryCommentRequest
import com.young.domain.post.dto.request.UpdateInquiryRequest
import com.young.domain.post.service.InquiryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/inquiries")
class InquiryController(
    private val inquiryService: InquiryService
) {

    @PostMapping
    fun createInquiry(@RequestBody request: CreateInquiryRequest) = inquiryService.createInquiry(request)

    @GetMapping
    fun getAllInquiries() = inquiryService.getAllInquiries()

    @GetMapping("/{inquiryId}")
    fun getInquiry(@PathVariable inquiryId: Long) = inquiryService.getInquiry(inquiryId)

    @PutMapping("/{inquiryId}")
    fun updateInquiry(@PathVariable inquiryId: Long, @RequestBody request: UpdateInquiryRequest)
    = inquiryService.updateInquiry(inquiryId, request)

    @DeleteMapping("/{inquiryId}")
    fun deleteInquiry(@PathVariable inquiryId: Long) = inquiryService.deleteInquiry(inquiryId)

    @PostMapping("/comments")
    fun createComment(@RequestBody request: CreateInquiryCommentRequest) = inquiryService.createComment(request)

    @GetMapping("/comments/{inquiryId}")
    fun getComments(@PathVariable inquiryId: Long) = inquiryService.getCommentsByInquiry(inquiryId)

    @PutMapping("/comments/{commentId}")
    fun updateComment(@PathVariable commentId: Long, @RequestBody request: UpdateInquiryCommentRequest)
    = inquiryService.updateComment(commentId, request)

    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(@PathVariable commentId: Long) = inquiryService.deleteComment(commentId)
}