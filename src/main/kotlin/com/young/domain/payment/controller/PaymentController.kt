package com.young.domain.payment.controller

import com.young.domain.payment.dto.request.PayRequest
import com.young.domain.payment.dto.request.PaymentCancelRequest
import com.young.domain.payment.service.PaymentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "결제", description = "결제 api")
@RestController
@RequestMapping("/payment")
class PaymentController (
    private val paymentService: PaymentService
) {
    @Operation(summary = "결제 승인", description = "결제를 승인합니다.")
    @PostMapping("/confirm")
    fun confirmPayment(@RequestBody request: PayRequest) = paymentService.confirmPayment(request)

    @Operation(summary = "결제 취소", description = "결제를 취소합니다.")
    @DeleteMapping("/cancel")
    fun cancelPayment(@RequestBody request: PaymentCancelRequest) = paymentService.cancelPayment(request)
}