package com.young.domain.auth.controller

import com.young.domain.auth.dto.request.GoogleLoginRequest
import com.young.domain.auth.service.OAuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "오픈 인증", description = "오픈 인증(OAuth) api")
@RestController
@RequestMapping("/oauth")
class OAuthController (
    private val oAuthService: OAuthService
) {
    @Operation(summary = "구글 로그인", description = "구글 로그인 합니다.")
    @PostMapping("/google")
    fun login(@RequestBody request: GoogleLoginRequest) = oAuthService.login(request)
}