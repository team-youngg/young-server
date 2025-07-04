package com.young.domain.image.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
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
import java.util.*

@Service
class ImageService(
    private val amazonS3: AmazonS3,
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String,
    private val bannerImageRepository: BannerImageRepository
) {
    @Transactional
    fun uploadImage(file: MultipartFile): ImageResponse {
        val filename = "${UUID.randomUUID()}-${file.originalFilename}"

        val metadata = ObjectMetadata()
        metadata.contentType = file.contentType
        metadata.contentLength = file.size

        amazonS3.putObject(bucket, filename, file.inputStream, metadata)

        return ImageResponse(amazonS3.getUrl(bucket, filename).toString())
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