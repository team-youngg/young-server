package com.young.domain.post.controller

import com.young.domain.post.service.InquiryService
import org.springframework.stereotype.Controller

@Controller
class InquiryController (
    private val inquiryService: InquiryService
) {

}