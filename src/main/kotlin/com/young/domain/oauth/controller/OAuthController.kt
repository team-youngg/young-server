package com.young.domain.oauth.controller

import com.young.domain.oauth.dto.request.GoogleLoginRequest
import com.young.domain.oauth.service.OAuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oauth")
class OAuthController (
    private val oAuthService: OAuthService
) {
    @PostMapping("/google")
    fun login(@RequestBody request: GoogleLoginRequest) = oAuthService.login(request)
}