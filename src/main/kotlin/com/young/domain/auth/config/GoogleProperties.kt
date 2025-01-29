package com.young.domain.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
data class GoogleProperties (
    val clientId: String,
    val clientSecret: String,
)