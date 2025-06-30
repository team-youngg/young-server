package com.young.domain.payment.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import java.nio.charset.StandardCharsets
import java.util.Base64

@Configuration
class TossClientConfig(
    private val paymentProperties: PaymentProperties
) {
    @Bean
    fun tossClient(): WebClient {
        val authHeader = "Basic " + Base64.getEncoder()
            .encodeToString("${paymentProperties.apiSecret}:".toByteArray(StandardCharsets.UTF_8))

        return WebClient.builder()
            .baseUrl("https://api.tosspayments.com/v1")
            .defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}