package com.young.domain.oauth.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.young.domain.oauth.config.GoogleProperties
import com.young.domain.oauth.dto.GoogleLoginAuthDto
import com.young.domain.oauth.dto.GoogleTokenDto
import com.young.domain.oauth.dto.GoogleUserProfileDto
import com.young.domain.user.domain.entity.User
import com.young.domain.user.domain.enums.UserRole
import com.young.domain.user.error.UserError
import com.young.domain.user.repository.UserRepository
import com.young.global.exception.CustomException
import com.young.global.security.jwt.dto.JwtResponse
import com.young.global.security.jwt.provider.JwtProvider
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class OAuth2UserAuthCodeServiceImpl(
    private val userRepository: UserRepository,
    private val googleProperties: GoogleProperties,
    private val jwtProvider: JwtProvider
) {
    val clientId = googleProperties.clientId
    val clientSecret = googleProperties.clientSecret

    fun getGoogleAccessToken(googleLoginAuthDto: GoogleLoginAuthDto): GoogleTokenDto.Companion.Response {
        val requestUrl = "https://oauth2.googleapis.com/token" //todo 동적 참조

        val restTemplate = RestTemplate()

        val headers = HttpHeaders()
        headers.set("Content-Type", "application/x-www-form-urlencoded")

        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        val code = googleLoginAuthDto.code
        val redirectUri = googleLoginAuthDto.redirectUri

        params.add("code", code)
        params.add("client_id", clientId)
        params.add("client_secret", clientSecret)
        params.add("redirect_uri", redirectUri)
        params.add("grant_type", "authorization_code")
        params.add("scope", "email profile openid")

        println("Request URL: $requestUrl")
        println("Request Headers: $headers")
        println("Request Parameters: $params")

        val responseEntity = restTemplate.postForEntity(requestUrl, HttpEntity(params, headers), String::class.java)
        println("Response: ${responseEntity.body}")

        if (responseEntity.statusCode == HttpStatus.OK) {
            return responseEntity.body.let{
                ObjectMapper().readValue(it, GoogleTokenDto.Companion.Response::class.java)
            }
        } else {
            throw RuntimeException("Google Response error")
        }
    }

    fun getGoogleUserProfile(accessToken: String): GoogleUserProfileDto {
        val requestUrl = "https://www.googleapis.com/oauth2/v2/userinfo"

        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")

        val requestEntity = HttpEntity<Unit>(headers)
        val responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity, String::class.java)

        if (!responseEntity.statusCode.is2xxSuccessful) {
            throw RuntimeException("Failed to fetch user profile from Google")
        }

        val objectMapper = ObjectMapper()
        val jsonNode: JsonNode = objectMapper.readTree(responseEntity.body)

        return GoogleUserProfileDto(
//            id = jsonNode["id"].asText(),
            email = jsonNode["email"].asText(),
            name = jsonNode["name"].asText(),
            picture = jsonNode["picture"].asText()
        )
    }

    fun saveOAuthUserProfileAndUser(googleUserProfile: GoogleUserProfileDto) : JwtResponse {
//        if (userRepository.existsByEmail(googleUserProfile.email)) throw CustomException(UserError.USERNAME_DUPLICATED)
        // 기존 사용자 확인
        val existingUser = userRepository.findByEmail(googleUserProfile.email)

        if (existingUser == null) {
            // 새로운 사용자 저장
            val newUser = User(
                email = googleUserProfile.email,
                username = googleUserProfile.name,
//                profileImageUrl = googleUserProfile.picture,
//                oauthProvider = "GOOGLE"
                password = "",
                role = UserRole.USER,
            )
            userRepository.save(newUser)
            println("New user saved: ${newUser.email}")
            return jwtProvider.generateToken(newUser)
        } else {
            println("User already exists: ${existingUser.email}")
            return jwtProvider.generateToken(existingUser)
        }
    }

}