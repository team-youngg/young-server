package com.young.domain.user.service

import com.young.domain.user.dto.response.UserResponse
import com.young.domain.user.error.UserError
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val securityHolder: SecurityHolder
) {
    @Transactional
    fun getMe(): UserResponse {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        return UserResponse.of(user)
    }
}