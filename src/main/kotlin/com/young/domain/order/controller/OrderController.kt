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
    @Operation(summary = "주문", description = "상품을 주문합니다.")
    @PostMapping
    fun orderFromCart(@RequestBody requests: OrderManyRequest) = orderService.order(requests)
}