package com.young.domain.order.controller

import com.young.domain.order.dto.request.OrderManyRequest
import com.young.domain.order.service.OrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "주문", description = "주문 api")
@RestController
@RequestMapping("/orders")
class OrderController (
    private val orderService: OrderService
) {
    // TODO (웹) 결제 승인하기 버튼 눌르면 오더 요청보내기
    // TODO (서버) 받을 요청은... 유저랑,

    @Operation(summary = "주문", description = "상품을 주문합니다.")
    @PostMapping
    fun orderFromCart(@RequestBody requests: OrderManyRequest) = orderService.order(requests)
}