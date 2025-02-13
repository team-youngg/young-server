package com.young.domain.review.repository

import com.young.domain.review.domain.entity.ReviewImage
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewImageRepository : JpaRepository<ReviewImage, Long> {
}