package com.young.domain.image.repository

import com.young.domain.image.domain.entity.BannerImage
import org.springframework.data.jpa.repository.JpaRepository

interface BannerImageRepository : JpaRepository<BannerImage, Long> {
}