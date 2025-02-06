package com.young.domain.user.controller

import com.young.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "유저", description = "유저 api")
@RestController
@RequestMapping("/users")
class UserController (
    private val userService: UserService
) {
    @Operation(summary = "나 조회", description = "나 조회하기")
    @GetMapping("/me")
    fun getMe() = userService.getMe()

    @Operation(summary = "주문 정보 조회", description = "주문 정보 조회하기")
    @GetMapping("/order-info")
    fun getOrderInfo() = userService.getOrderInfo()
}