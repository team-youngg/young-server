package com.young.domain.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google-admin")
data class GoogleAdminProperties (
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
)