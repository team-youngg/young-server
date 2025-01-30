package com.young.domain.cart.repository

import com.young.domain.cart.domain.entity.Cart
import com.young.domain.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface CartRepository : JpaRepository<Cart, Long> {
    fun existsByUser(user: User): Boolean
    fun findByUser(user: User): Cart?
}