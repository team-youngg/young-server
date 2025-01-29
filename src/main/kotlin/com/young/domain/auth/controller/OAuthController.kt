package com.young.domain.auth.controller

import com.young.domain.auth.dto.request.GoogleLoginRequest
import com.young.domain.auth.service.OAuthService
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