package com.young.domain.user.dto.response

import com.young.domain.user.domain.entity.User

data class UserResponse(
    val email: String,
    val username: String,
) {
    companion object {
        fun of(user: User) = UserResponse(
            email = user.email,
            username = user.username,
        )
    }
}