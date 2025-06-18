package com.young.domain.image.dto.response

import com.young.domain.image.domain.entity.BannerImage


data class ImageResponse(
    val url: String,
) {
    companion object {
        fun of(image: BannerImage): ImageResponse = ImageResponse(
            url = image.url
        )
    }
}