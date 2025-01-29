package com.young.global.security.jwt.provider

import com.young.domain.auth.repository.RefreshTokenRepository
import com.young.domain.user.domain.entity.User
import com.young.domain.user.error.UserError
import com.young.domain.user.repository.UserRepository
import com.young.global.exception.CustomException
import com.young.global.security.CustomUserDetails
import com.young.global.security.jwt.config.JwtProperties
import com.young.global.security.jwt.dto.JwtResponse
import com.young.global.security.jwt.enums.JwtType
import com.young.global.security.jwt.error.JwtError
import io.jsonwebtoken.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.spec.SecretKeySpec


@Component
class JwtProvider(
    private val jwtProperties: JwtProperties,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    private val key = SecretKeySpec(
        jwtProperties.secretKey.toByteArray(Charsets.UTF_8),
        Jwts.SIG.HS512.key().build().algorithm
    )

    fun generateToken(user: User): JwtResponse {
        val now = Date()

        val accessToken = Jwts.builder()
            .header()
            .type(JwtType.ACCESS.name)
            .and()
            .subject(user.email)
            .issuedAt(now)
            .issuer(jwtProperties.issuer)
            .expiration(Date(now.time + jwtProperties.accessExp))
            .signWith(key)
            .compact()

        val refreshToken = Jwts.builder()
            .header()
            .type(JwtType.REFRESH.name)
            .and()
            .subject(user.email)
            .issuedAt(now)
            .issuer(jwtProperties.issuer)
            .expiration(Date(now.time + jwtProperties.refreshExp))
            .signWith(key)
            .compact()

        refreshTokenRepository.setRefreshToken(user.email, refreshToken)

        return JwtResponse(accessToken, refreshToken)
    }

    fun getEmail(token: String): String = getClaims(token).subject

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)
        val user = userRepository.findByEmail(claims.subject) ?: throw CustomException(UserError.USER_NOT_FOUND)
        val details = CustomUserDetails(user)

        return UsernamePasswordAuthenticationToken(details, null, details.authorities)
    }

    fun extractToken(request: HttpServletRequest) =
        request.getHeader("Authorization")?.removePrefix("Bearer ")

    fun getType(token: String) = JwtType.valueOf(
        Jwts.parser()
            .verifyWith(key)
            .requireIssuer(jwtProperties.issuer)
            .build()
            .parseSignedClaims(token)
            .header.type
    )

    private fun getClaims(token: String): Claims {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .requireIssuer(jwtProperties.issuer)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            throw CustomException(JwtError.EXPIRED_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw CustomException(JwtError.UNSUPPORTED_TOKEN)
        } catch (e: MalformedJwtException) {
            throw CustomException(JwtError.MALFORMED_TOKEN)
        } catch (e: SecurityException) {
            throw CustomException(JwtError.INVALID_TOKEN)
        } catch (e: IllegalArgumentException) {
            throw CustomException(JwtError.INVALID_TOKEN)
        }
    }
}