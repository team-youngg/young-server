package com.young.domain.user.dto.response

import com.young.domain.user.domain.entity.User

data class UserResponse(
    val username: String,
) {
    companion object {
        fun of(user: User) = com.young.domain.user.dto.response.UserResponse(
            username = user.username,
        )
    }
}