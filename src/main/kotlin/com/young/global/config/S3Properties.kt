package com.young.global.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cloud.aws.credentials")
data class S3Properties (
    val accessKey: String,
    val secretKey: String,
)