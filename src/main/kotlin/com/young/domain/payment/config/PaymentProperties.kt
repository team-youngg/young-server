package com.young.domain.payment.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "toss")
data class PaymentProperties (
    val apiSecret: String,
)