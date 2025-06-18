package com.young.domain.image.controller

import com.young.domain.image.dto.request.UpdateBannerRequest
import com.young.domain.image.service.ImageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/banner")
@Tag(name = "배너 사진", description = "Banner Image Controller")
class BannerImageController (
    private val imageService: ImageService,
) {
    @PutMapping("/{imageId}")
    @Operation(summary = "배너 이미지 수정", description = "배너 이미지를 수정합니다.")
    fun updateImage(@PathVariable imageId: Long, @RequestBody request: UpdateBannerRequest)
    = imageService.updateBanner(imageId, request)

    @GetMapping
    @Operation(summary = "배너 이미지 조회", description = "배너 이미지를 조회합니다.")
    fun getImages() = imageService.getBannerImages()
}