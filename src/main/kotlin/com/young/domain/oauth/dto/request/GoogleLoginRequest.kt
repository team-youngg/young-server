package com.young.domain.oauth.dto.request

data class GoogleLoginRequest(
    val code: String,
    val redirectUri: String,
)