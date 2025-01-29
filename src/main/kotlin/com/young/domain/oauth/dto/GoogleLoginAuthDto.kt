package com.young.domain.oauth.dto

data class GoogleLoginAuthDto(
    val code: String,
    val redirectUri: String,
) {
    companion object {
        data class Request(
            val code: String,
            val redirectUri: String
        )

        fun fromRequest(request: Request): GoogleLoginAuthDto {
            return GoogleLoginAuthDto(
                code = request.code,
                redirectUri = request.redirectUri
            )
        }
    }
}
