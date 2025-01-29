package com.young.domain.user.domain.entity

import com.young.domain.user.domain.enums.UserRole
import com.young.global.common.BaseEntity
import jakarta.persistence.*


@Entity
@Table(name = "users")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "username", nullable = false)
    val username: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole,

    @Column(name = "email", nullable = false, unique = true)
    val email: String,
) : BaseEntity()