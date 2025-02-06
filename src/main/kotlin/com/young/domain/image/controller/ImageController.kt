package com.young.domain.image.controller

import com.young.domain.image.service.ImageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "이미지", description = "이미지 api")
@RestController
@RequestMapping("/image")
class ImageController (
    private val imageService: ImageService
) {
    @Operation(summary = "이미지 업로드", description = "이미지를 업로드 합니다.")
    @PostMapping("/images", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(@RequestPart image: MultipartFile) = imageService.uploadImage(image)
}