package com.young.domain.review.dto.response

import com.young.domain.review.domain.entity.Review
import com.young.domain.user.dto.response.UserResponse

data class ReviewResponse(
    val comment: String,
    val star: Float,
    val author: UserResponse,
    val images: List<String>,
) {
    companion object {
        fun of(review: Review): ReviewResponse {
            return ReviewResponse(
                comment = review.comment,
                star = review.star,
                author = UserResponse.of(review.author),
                images = review.images.map { it.url }
            )
        }
    }
}