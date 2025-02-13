package com.young.domain.review.service

import com.young.domain.item.error.ItemError
import com.young.domain.option.repository.ItemOptionRepository
import com.young.domain.review.domain.entity.Review
import com.young.domain.review.domain.entity.ReviewImage
import com.young.domain.review.dto.request.CreateReviewRequest
import com.young.domain.review.repository.ReviewImageRepository
import com.young.domain.review.repository.ReviewRepository
import com.young.domain.user.error.UserError
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReviewService (
    private val reviewRepository: ReviewRepository,
    private val securityHolder: SecurityHolder,
    private val itemOptionRepository: ItemOptionRepository,
    private val reviewImageRepository: ReviewImageRepository
) {
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
        reviewRepository.save(review)

        val reviewImages = request.images.map { ReviewImage(url = it, review = review) }
        reviewImageRepository.saveAll(reviewImages)
    }
}