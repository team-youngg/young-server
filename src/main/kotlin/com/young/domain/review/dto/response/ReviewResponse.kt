package com.young.domain.review.dto.response

import com.young.domain.item.dto.response.ItemOptionValueResponse
import com.young.domain.option.domain.entity.ItemOptionValue
import com.young.domain.review.domain.entity.Review
import com.young.domain.user.dto.response.UserResponse
import java.time.LocalDateTime

data class ReviewResponse(
    val comment: String,
    val star: Float,
    val author: UserResponse,
    val images: List<String>,
    val option: List<ItemOptionValueResponse>,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(review: Review, itemOptionValues: List<ItemOptionValue>): ReviewResponse {
            return ReviewResponse(
                comment = review.comment,
                star = review.star,
                author = UserResponse.of(review.author),
                images = review.images.map { it.url },
                option = itemOptionValues.map { ItemOptionValueResponse.of(it) },
                createdAt = review.createdAt,
            )
        }
    }
}