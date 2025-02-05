package com.young.domain.auth.controller

import com.young.domain.auth.dto.request.ReissueRequest
import com.young.domain.auth.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "인증", description = "인증 api")
@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다.")
    @PostMapping("/reissue")
    fun reissue(@RequestBody request: ReissueRequest) = authService.reissue(request)
}