package com.young.global.security

import com.young.domain.user.domain.entity.User
import com.young.domain.user.error.UserError
import com.young.domain.user.repository.UserRepository
import com.young.global.exception.CustomException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component


@Component
class SecurityHolder(
    private val userRepository: UserRepository,
) {
    val user: User
        get() = userRepository.findByEmail(SecurityContextHolder.getContext().authentication.name)
            ?: throw CustomException(UserError.USER_NOT_FOUND)
}