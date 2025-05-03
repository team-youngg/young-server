package com.young.domain.image.service

import com.young.domain.image.dto.response.ImageResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.File
import java.util.*

@Service
class ImageService (
    @Value("\${spring.upload.dir}") private val uploadDir: String,
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

        return ImageResponse("https://youngg.store/uploads/${filename}")
    }
}