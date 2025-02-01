package com.young.domain.payment.controller

import com.young.domain.payment.dto.request.PayRequest
import com.young.domain.payment.service.PaymentService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PaymentController (
    private val paymentService: PaymentService
) {
    @RequestMapping("/payment/confirm")
    fun confirmPayment(@RequestBody request: PayRequest) = paymentService.confirmPayment(request)
}