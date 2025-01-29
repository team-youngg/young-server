package com.young.domain.auth.controller

import com.young.domain.auth.dto.request.ReissueRequest
import com.young.domain.auth.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/reissue")
    fun reissue(@RequestBody request: ReissueRequest) = authService.reissue(request)
}