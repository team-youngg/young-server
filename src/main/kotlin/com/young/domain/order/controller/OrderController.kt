package com.young.domain.order.controller

import com.young.domain.order.dto.request.OrderManyRequest
import com.young.domain.order.dto.request.UpdateOrderOptionRequest
import com.young.domain.order.service.OrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
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

    @Operation(summary = "옵션 수정", description = "상품의 옵션을 수정합니다.")
    @PatchMapping
    fun updateOption(@RequestBody request: UpdateOrderOptionRequest) = orderService.updateOrderOption(request)

    @Operation(summary = "주문 기록 조회", description = "내 주문 기록을 조회합니다.")
    @GetMapping("/my")
    fun getOrders(@PageableDefault pageable: Pageable) = orderService.getOrderLogs(pageable)
}