package com.young.domain.review.controller

import com.young.domain.review.dto.request.CreateReviewRequest
import com.young.domain.review.dto.request.UpdateReviewRequest
import com.young.domain.review.service.ReviewService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "리뷰", description = "리뷰 api")
@RestController
@RequestMapping("/reviews")
class ReviewController (
    private val reviewService: ReviewService
) {
    @PostMapping
    fun createReview(request: CreateReviewRequest) = reviewService.createReview(request)

    @GetMapping("/{itemId}")
    fun getReviews(@PathVariable itemId: Long) = reviewService.getReviews(itemId)

    @GetMapping("/my")
    fun getMyReviews() = reviewService.getMyReviews()

    @PatchMapping("/{reviewId}")
    fun updateReview(@RequestBody request: UpdateReviewRequest, @PathVariable reviewId: Long)
    = reviewService.updateReview(request, reviewId)

    @DeleteMapping("/{reviewId}")
    fun deleteReview(@PathVariable reviewId: Long) = reviewService.deleteReview(reviewId)
}