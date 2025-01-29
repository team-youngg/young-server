package com.young.domain.auth.service

import com.young.domain.auth.dto.request.ReissueRequest
import com.young.domain.auth.repository.RefreshTokenRepository
import com.young.domain.user.error.UserError
import com.young.domain.user.repository.UserRepository
import com.young.global.exception.CustomException
import com.young.global.security.jwt.dto.JwtResponse
import com.young.global.security.jwt.enums.JwtType
import com.young.global.security.jwt.error.JwtError
import com.young.global.security.jwt.provider.JwtProvider
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service


@Service
class AuthService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
) {
    @Transactional
    fun reissue(request: ReissueRequest): JwtResponse {
        if (jwtProvider.getType(request.refreshToken) != JwtType.REFRESH) {
            throw CustomException(JwtError.INVALID_TOKEN_TYPE)
        }

        val email = jwtProvider.getEmail(request.refreshToken)
        val user = userRepository.findByEmail(email) ?: throw CustomException(UserError.USER_NOT_FOUND)
        val refreshToken =
            refreshTokenRepository.getRefreshToken(email) ?: throw CustomException(JwtError.INVALID_TOKEN)

        if (refreshToken != request.refreshToken) {
            throw CustomException(JwtError.INVALID_TOKEN)
        }

        return jwtProvider.generateToken(user)
    }
}