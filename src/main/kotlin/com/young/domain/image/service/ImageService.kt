package com.young.domain.image.service

import com.young.domain.image.dto.request.UpdateBannerRequest
import com.young.domain.image.dto.response.ImageResponse
import com.young.domain.image.error.ImageError
import com.young.domain.image.repository.BannerImageRepository
import com.young.global.exception.CustomException
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

@Service
class ImageService (
    @Value("\${spring.upload.dir}") private val uploadDir: String,
    private val bannerImageRepository: BannerImageRepository
) {
    @Transactional
    fun uploadImage(file: MultipartFile) : ImageResponse {
        val filename = "${UUID.randomUUID()}-${file.originalFilename}"

        val directory = File(uploadDir)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val targetFile = File(directory, filename)
        file.transferTo(targetFile)

        return ImageResponse("https://api.ylabpoint.store/uploads/${filename}")
    }

    @Transactional
    fun updateBanner(id: Long, request: UpdateBannerRequest) {
        val image = bannerImageRepository.findByIdOrNull(id) ?: throw CustomException(ImageError.IMAGE_NOT_FOUND)
        image.url = request.url
        bannerImageRepository.save(image)
    }

    @Transactional
    fun getBannerImages(): List<ImageResponse> {
        return bannerImageRepository.findAll().sortedBy { it.id }.map { ImageResponse.of(it) }.toList()
    }
}