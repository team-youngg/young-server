package com.young.global.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class S3Config(
    private val s3Properties: S3Properties,
    @Value("\${cloud.aws.region.static}") val region: String,
) {
    @Bean
    fun amazonS3Client(): AmazonS3 {
        val awsCredentials = BasicAWSCredentials(s3Properties.accessKey, s3Properties.secretKey)
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
            .build()
    }
}