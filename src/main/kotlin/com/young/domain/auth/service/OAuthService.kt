package com.young.domain.auth.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.young.domain.auth.config.GoogleProperties
import com.young.domain.auth.dto.request.GoogleLoginRequest
import com.young.domain.auth.dto.response.GoogleUserResponse
import com.young.domain.cart.domain.entity.Cart
import com.young.domain.cart.repository.CartRepository
import com.young.domain.user.domain.entity.User
import com.young.domain.user.domain.enums.UserRole
import com.young.domain.user.repository.UserRepository
import com.young.global.security.jwt.dto.JwtResponse
import com.young.global.security.jwt.provider.JwtProvider
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class OAuthService (
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository,
    private val googleProperties: GoogleProperties,
    private val webClient: WebClient.Builder,
    private val cartRepository: CartRepository,
) {
    @Transactional
    fun login(request: GoogleLoginRequest): JwtResponse {
        val googleAccessToken = getGoogleAccessToken(request)
        val googleUser = getGoogleUser(googleAccessToken)

        val user = User(
            email = googleUser.email,
            username = googleUser.username,
            role = UserRole.USER,
            avatar = googleUser.avatar
        )

        if (!userRepository.existsByEmail(googleUser.email)) {
            userRepository.save(user)

            val cart = Cart(
                user = user,
            )
            cartRepository.save(cart)
        }

        return jwtProvider.generateToken(user)
    }

    @Transactional
    fun getGoogleAccessToken(request: GoogleLoginRequest): String {
        val requestUrl = "https://oauth2.googleapis.com/token"
        val redirectUri = "http://localhost:3000/auth/google/callback"

        val formData: MultiValueMap<String, String> = LinkedMultiValueMap()
        formData.add("code", request.code)
        formData.add("client_id", googleProperties.clientId)
        formData.add("client_secret", googleProperties.clientSecret)
        formData.add("redirect_uri", redirectUri)
        formData.add("grant_type", "authorization_code")

        return webClient.build()
            .post()
            .uri(requestUrl)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .bodyValue(formData)
            .retrieve()
            .onStatus({ status -> !status.is2xxSuccessful }) { response ->
                response.bodyToMono(String::class.java)
                    .flatMap { body -> Mono.error(RuntimeException("Google Response error: $body")) }
            }
            .bodyToMono(String::class.java)
            .map { responseBody ->
                ObjectMapper().readTree(responseBody).get("access_token").asText()
            }
            .block()!!
    }

    fun getGoogleUser(googleAccessToken: String): GoogleUserResponse {
        val requestUrl = "https://www.googleapis.com/oauth2/v2/userinfo"

        return webClient.build()
            .get()
            .uri(requestUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $googleAccessToken")
            .retrieve()
            .onStatus({ status -> !status.is2xxSuccessful }) { response ->
                response.bodyToMono(String::class.java)
                    .flatMap { body -> Mono.error(RuntimeException("Failed to fetch user profile from Google: $body")) }
            }
            .bodyToMono(String::class.java)
            .map { responseBody ->
                val jsonNode = ObjectMapper().readTree(responseBody)

                GoogleUserResponse(
                    email = jsonNode["email"].asText(),
                    username = jsonNode["name"].asText(),
                    avatar = jsonNode["picture"].asText(),
                )
            }
            .block()!!
    }

}