package com.young.domain.review.controller

import com.young.domain.review.dto.request.CreateReviewRequest
import com.young.domain.review.dto.request.UpdateReviewRequest
import com.young.domain.review.service.ReviewService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "리뷰", description = "리뷰 api")
@RestController
@RequestMapping("/reviews")
class ReviewController (
    private val reviewService: ReviewService
) {
    @Operation(summary = "리뷰 생성", description = "리뷰를 생성합니다.")
    @PostMapping
    fun createReview(@RequestBody request: CreateReviewRequest) = reviewService.createReview(request)

    @Operation(summary = "리뷰 조회", description = "상품별로 리뷰를 조회합니다.")
    @GetMapping("/{itemId}")
    fun getReviews(@PathVariable itemId: Long) = reviewService.getReviews(itemId)

    @Operation(summary = "내 리뷰 조회", description = "내가 작성한 리뷰를 조회합니다.")
    @GetMapping("/my")
    fun getMyReviews() = reviewService.getMyReviews()

    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    @PatchMapping("/{reviewId}")
    fun updateReview(@RequestBody request: UpdateReviewRequest, @PathVariable reviewId: Long)
    = reviewService.updateReview(request, reviewId)

    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @DeleteMapping("/{reviewId}")
    fun deleteReview(@PathVariable reviewId: Long) = reviewService.deleteReview(reviewId)
}