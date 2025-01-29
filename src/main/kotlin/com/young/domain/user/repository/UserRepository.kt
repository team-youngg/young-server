package com.young.domain.user.repository

import com.young.domain.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}