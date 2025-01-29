package com.young.domain.item.repository

import com.young.domain.item.domain.entity.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<Image, Long> {
}