package com.young.domain.user.service

import com.young.domain.user.dto.response.OrderInfoResponse
import com.young.domain.user.dto.response.UserResponse
import com.young.domain.user.error.UserError
import com.young.domain.user.repository.UserOrderInfoRepository
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val securityHolder: SecurityHolder,
    private val userOrderInfoRepository: UserOrderInfoRepository
) {
    fun getMe(): UserResponse {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        return UserResponse.of(user)
    }

    fun getOrderInfo(): List<OrderInfoResponse> {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val orderInfos = userOrderInfoRepository.findAllByUser(user)
        return orderInfos.map { OrderInfoResponse.of(it) }
    }
}