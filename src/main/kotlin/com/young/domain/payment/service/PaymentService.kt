package com.young.domain.payment.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.young.domain.payment.config.paymentProperties
import com.young.domain.payment.dto.request.PayRequest
import com.young.domain.payment.dto.response.PaymentResponse
import com.young.domain.payment.error.PaymentError
import com.young.global.exception.CustomException
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.util.Base64

@Service
class PaymentService (
    private val paymentProperties: paymentProperties,
) {
    fun getHeader(): String {
        val authorizations = "Basic " + Base64.getEncoder()
            .encodeToString("${paymentProperties.apiSecret}:".toByteArray(StandardCharsets.UTF_8))
        return authorizations
    }

    fun confirmPayment(request: PayRequest) : PaymentResponse {
        val webClient = WebClient.builder()
            .baseUrl("https://api.tosspayments.com/v1")
            .defaultHeader("Authorization", getHeader())
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .build()

        val objectMapper = jacksonObjectMapper()

        val response: String = webClient.post()
            .uri("/payments/confirm")
            .bodyValue(request)
            .retrieve()
            .onStatus({ it.isError }) { response ->
                response.bodyToMono(String::class.java).flatMap {
                    Mono.error(CustomException(PaymentError.API_ERROR, it.toString()))
                }
            }
            .bodyToMono(String::class.java)
            .block() ?: throw CustomException(PaymentError.API_ERROR, "No response")

        println("🔎 Toss API 응답 데이터: $response")

        return objectMapper.readValue(response, PaymentResponse::class.java)
    }
}