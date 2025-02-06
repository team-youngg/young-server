package com.young.domain.user.repository

import com.young.domain.user.domain.entity.User
import com.young.domain.user.domain.entity.UserOrderInfo
import org.springframework.data.jpa.repository.JpaRepository

interface UserOrderInfoRepository : JpaRepository<UserOrderInfo, Long> {
    fun findAllByUser(user: User): List<UserOrderInfo>
    fun findByUserAndIsDefaultTrue(user: User): UserOrderInfo?
}