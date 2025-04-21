package com.young.domain.review.service

import com.young.domain.item.error.ItemError
import com.young.domain.item.repository.ItemRepository
import com.young.domain.option.repository.ItemOptionRepository
import com.young.domain.option.repository.ItemOptionValueRepository
import com.young.domain.order.error.OrderError
import com.young.domain.order.repository.OrderItemOptionRepository
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
    private val itemRepository: ItemRepository,
    private val itemOptionValueRepository: ItemOptionValueRepository,
    private val orderItemOptionRepository: OrderItemOptionRepository,
) {
    @Transactional
    fun createReview(request: CreateReviewRequest) {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val orderItemOption = orderItemOptionRepository.findByIdOrNull(request.orderItemOptionId)
            ?: throw CustomException(OrderError.ORDER_NOT_FOUND)

        val review = Review(
            author = user,
            itemOption = orderItemOption,
            comment = request.comment,
            star = request.star,
        )
        val images = request.images.map { ReviewImage(url = it, review = review) }

        review.addImages(images)
        reviewRepository.save(review)
    }

    @Transactional(readOnly = true)
    fun getReviews(itemId: Long): List<ReviewResponse> {
        val item = itemRepository.findByIdOrNull(itemId)
            ?: throw CustomException(ItemError.ITEM_NOT_FOUND)

        val reviews = reviewRepository.findAllByItemOption_Item(item)

        return reviews.map {
            val values = itemOptionValueRepository.findAllByItemOption(it.itemOption.itemOption)
            ReviewResponse.of(it, values)
        }
    }

    @Transactional(readOnly = true)
    fun getMyReviews(): List<ReviewResponse> {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val reviews = reviewRepository.findAllByAuthor(user)

        return reviews.map {
            val itemOptionValues = itemOptionValueRepository.findAllByItemOption(it.itemOption.itemOption)
            ReviewResponse.of(it, itemOptionValues)
        }
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