package com.young.domain.review.service

import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.ItemRepository
import com.young.domain.option.repository.ItemOptionRepository
import com.young.domain.review.domain.entity.Review
import com.young.domain.review.domain.entity.ReviewImage
import com.young.domain.review.dto.request.CreateReviewRequest
import com.young.domain.review.dto.request.UpdateReviewRequest
import com.young.domain.review.dto.response.ReviewResponse
import com.young.domain.review.error.ReviewError
import com.young.domain.review.repository.ReviewRepository
import com.young.domain.user.error.UserError
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReviewService (
    private val reviewRepository: ReviewRepository,
    private val securityHolder: SecurityHolder,
    private val itemOptionRepository: ItemOptionRepository,
    private val itemRepository: ItemRepository
) {
    @Transactional
    fun createReview(request: CreateReviewRequest) {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val itemOption = itemOptionRepository.findByIdOrNull(request.optionId)
            ?: throw CustomException(ItemError.OPTION_NOT_FOUND)

        val review = Review(
            author = user,
            itemOption = itemOption,
            comment = request.comment,
            star = request.star,
        )
        val images = request.images.map { ReviewImage(url = it, review = review) }

        review.addImages(images)
        reviewRepository.save(review)
    }

    @Transactional(readOnly = true)
    fun getReviews(itemId: Long): List<ReviewResponse> {
        val item = itemRepository.findByIdOrNull(itemId) ?: throw CustomException(ItemError.ITEM_NOT_FOUND)
        val itemOptions = itemOptionRepository.findAllByItem(item)
        val reviews = itemOptions.flatMap { reviewRepository.findAllByItemOption(it) }

        return reviews.map { ReviewResponse.of(it) }
    }

    @Transactional(readOnly = true)
    fun getMyReviews(): List<ReviewResponse> {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val reviews = reviewRepository.findAllByAuthor(user)

        return reviews.map { ReviewResponse.of(it) }
    }

    @Transactional
    fun updateReview(request: UpdateReviewRequest, reviewId: Long) {
        val review = reviewRepository.findByIdOrNull(reviewId) ?: throw CustomException(ReviewError.REVIEW_NOT_FOUND)

        review.comment = request.comment ?: review.comment
        review.star = request.star ?: review.star

        reviewRepository.save(review)
    }

    @Transactional
    fun deleteReview(reviewId: Long) {
        val review = reviewRepository.findByIdOrNull(reviewId) ?: throw CustomException(ReviewError.REVIEW_NOT_FOUND)
        reviewRepository.delete(review)
    }
}