package com.young.domain.oauth.dto

data class GoogleTokenDto(
    val access_token: String = "",
    val expires_in: Int = 0,
    val scope: String = "",
    val token_type: String = "",
    val id_token: String = "",
    val refresh_token: String = ""
) {
    companion object {
        data class Response(
            val access_token: String = "",
            val expires_in: Int = 0,
            val scope: String = "",
            val token_type: String = "",
            val id_token: String = "",
            val refresh_token: String = ""
        )

        fun fromResponse(response: Response): GoogleTokenDto {
            return GoogleTokenDto(
                response.access_token,
                response.expires_in,
                response.scope,
                response.token_type,
                response.id_token,
                response.refresh_token
            )
        }
    }
}
