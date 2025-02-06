package com.young.domain.info.controller

import com.young.domain.info.dto.request.CreateOrderInfoRequest
import com.young.domain.info.dto.request.UpdateOrderInfoRequest
import com.young.domain.info.service.UserOrderInfoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "주문 정보", description = "주문 정보 api")
@RestController
@RequestMapping("/order-info")
class UserOrderInfoController (
    private val userOrderInfoService: UserOrderInfoService
) {
    @Operation(summary = "주문 정보 조회", description = "내 주문 정보를 조회합니다.")
    @GetMapping("my")
    fun getOrderInfo() = userOrderInfoService.getOrderInfo()

    @Operation(summary = "주문 정보 생성", description = "주문 정보를 생성합니다.")
    @PostMapping
    fun createOrderInfo(@RequestBody request: CreateOrderInfoRequest) = userOrderInfoService.createOrderInfo(request)

    @Operation(summary = "주문 정보 수정", description = "주문 정보를 수정합니다.")
    @PatchMapping("/{infoId}")
    fun updateOrderInfo(@RequestBody request: UpdateOrderInfoRequest, @PathVariable infoId: Long)
    = userOrderInfoService.updateOrderInfo(request, infoId)

    @Operation(summary = "기본 배송지 수정", description = "기본 배송지를 수정합니다.")
    @PatchMapping("default/{infoId}")
    fun updateOrderInfoDefault(@PathVariable infoId: Long) = userOrderInfoService.updateOrderInfoDefault(infoId)
}