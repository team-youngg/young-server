package com.young.domain.review.dto.request

data class CreateReviewRequest(
    val optionId: Long,
    val comment: String,
    val star: Float,
    val images: List<String>
)