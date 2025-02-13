package com.young.domain.review.dto.request

data class CreateReviewRequest(
    val orderItemOptionId: Long,
    val comment: String,
    val star: Float,
    val images: List<String>
)