package com.young.domain.user.domain.entity

import com.young.domain.user.domain.enums.UserRole
import com.young.global.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.*
import jakarta.persistence.Table


@Entity
@Table(name = "users")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "username", nullable = false)
    val username: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole
) : BaseEntity()