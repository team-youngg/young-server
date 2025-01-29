package com.young.domain.oauth.dto

data class GoogleUserProfileDto(
    val sub: String = "",
    val name: String = "",
    val given_name: String = "",
    val family_name: String = "",
    val picture: String = "",
    val email: String ="",
    val email_verified: Boolean = false,
    val locale: String ="",
) {
    companion object {
        class Response(
            val sub: String = "",
            val name: String = "",
            val given_name: String = "",
            val family_name: String = "",
            val picture: String = "",
            val email: String = "",
            val email_verified: Boolean = false,
            val locale: String = "",
        )
    }
}