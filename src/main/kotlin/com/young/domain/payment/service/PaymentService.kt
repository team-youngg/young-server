package com.young.domain.payment.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.young.domain.payment.config.paymentProperties
import com.young.domain.payment.dto.request.PayRequest
import com.young.domain.payment.dto.response.PaymentResponse
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
    fun getAuth(): String {
        val authorizations = "Basic " + Base64.getEncoder()
            .encodeToString("${paymentProperties.apiSecret}:".toByteArray(StandardCharsets.UTF_8))
        return authorizations
    }

    fun confirmPayment(request: PayRequest) : PaymentResponse {
        val webClient = WebClient.builder()
            .baseUrl("https://api.tosspayments.com/v1")
            .defaultHeader("Authorization", getAuth())
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .build()

        val objectMapper = jacksonObjectMapper()

        val responseString: String = webClient.post()
            .uri("/payments/confirm")
            .bodyValue(request)
            .retrieve()
            .onStatus({ it.isError }) { response ->
                response.bodyToMono(String::class.java).flatMap {
                    Mono.error(RuntimeException("API Error: $it"))
                }
            }
            .bodyToMono(String::class.java)
            .block() ?: throw RuntimeException("API Error")

        println("🔍 Toss API 응답 데이터: $responseString")

        return objectMapper.readValue(responseString, PaymentResponse::class.java)
    }
}