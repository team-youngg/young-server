package com.young.domain.user.service

import com.young.domain.user.dto.response.UserResponse
import com.young.global.security.SecurityHolder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val securityHolder: SecurityHolder
) {
    fun getMe() : UserResponse {
        val user = securityHolder.user

        return UserResponse.of(user)
    }
}