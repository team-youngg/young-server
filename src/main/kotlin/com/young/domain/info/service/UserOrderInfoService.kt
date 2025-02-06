package com.young.domain.info.service

import com.young.domain.info.domain.entity.UserOrderInfo
import com.young.domain.info.dto.request.CreateOrderInfoRequest
import com.young.domain.info.dto.request.UpdateOrderInfoRequest
import com.young.domain.info.dto.response.OrderInfoResponse
import com.young.domain.info.error.InfoError
import com.young.domain.user.error.UserError
import com.young.domain.info.repository.UserOrderInfoRepository
import com.young.global.exception.CustomException
import com.young.global.security.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserOrderInfoService (
    private val securityHolder: SecurityHolder,
    private val userOrderInfoRepository: UserOrderInfoRepository
) {
    @Transactional
    fun getOrderInfo(): List<OrderInfoResponse> {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val orderInfos = userOrderInfoRepository.findAllByUser(user)
        return orderInfos.map { OrderInfoResponse.of(it) }
    }

    @Transactional
    fun createOrderInfo(request: CreateOrderInfoRequest) {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val defaultInfo = userOrderInfoRepository.findByUserAndIsDefaultTrue(user)

        if (request.isDefault && defaultInfo != null) {
            defaultInfo.isDefault = false
            userOrderInfoRepository.save(defaultInfo)
        }

        val orderInfo = UserOrderInfo(
            title = request.title,
            address = request.address,
            addressDetail = request.addressDetail,
            postCode = request.postCode,
            receiver = request.receiver,
            tel = request.tel,
            user = user,
            isDefault = request.isDefault,
        )
        userOrderInfoRepository.save(orderInfo)
    }

    @Transactional
    fun updateOrderInfo(request: UpdateOrderInfoRequest, infoId: Long) {
        val orderInfo = userOrderInfoRepository.findByIdOrNull(infoId)
            ?: throw CustomException(InfoError.INFO_NOT_FOUND)

        orderInfo.title = request.title ?: orderInfo.title
        orderInfo.address = request.address ?: orderInfo.address
        orderInfo.addressDetail = request.addressDetail ?: orderInfo.addressDetail
        orderInfo.postCode = request.postCode ?: orderInfo.postCode
        orderInfo.receiver = request.receiver ?: orderInfo.receiver
        orderInfo.tel = request.tel ?: orderInfo.tel

        userOrderInfoRepository.save(orderInfo)
    }

    fun updateOrderInfoDefault(infoId: Long) {
        val user = securityHolder.user ?: throw CustomException(UserError.USER_NOT_FOUND)
        val orderInfo = userOrderInfoRepository.findByIdOrNull(infoId)
            ?: throw CustomException(InfoError.INFO_NOT_FOUND)
        val defaultInfo = userOrderInfoRepository.findByUserAndIsDefaultTrue(user)

        if (defaultInfo != null) {
            defaultInfo.isDefault = false
            userOrderInfoRepository.save(defaultInfo)
        }

        orderInfo.isDefault = true
        userOrderInfoRepository.save(orderInfo)
    }
}