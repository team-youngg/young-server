package com.young.domain.oauth.controller

import com.young.domain.oauth.dto.GoogleLoginAuthDto
import com.young.domain.oauth.service.OAuth2UserAuthCodeServiceImpl
import com.young.global.security.jwt.dto.JwtResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oauth")
class OAuthController(
    private val oAuth2UserAuthCodeServiceImpl: OAuth2UserAuthCodeServiceImpl
) {
    // user auth code 로그인 처리
    @PostMapping("/google")
    fun authCodeLoginGoogle(@RequestBody googleLoginAuthDtoRequest: GoogleLoginAuthDto.Companion.Request): JwtResponse {
        val googleLoginAuthDto = GoogleLoginAuthDto.fromRequest(googleLoginAuthDtoRequest)
        val googleTokenResponse = oAuth2UserAuthCodeServiceImpl.getGoogleAccessToken(googleLoginAuthDto)
        val googleUserProfile = oAuth2UserAuthCodeServiceImpl.getGoogleUserProfile(googleTokenResponse.access_token)
        return oAuth2UserAuthCodeServiceImpl.saveOAuthUserProfileAndUser(googleUserProfile)
    }

}