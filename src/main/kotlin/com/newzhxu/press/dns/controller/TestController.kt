package com.newzhxu.press.dns.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author zheng2580369@gmail.com
 */
@RestController
@RequestMapping("/dns")

class TestController {
    @PreAuthorize("hasAnyAuthority('DNS_EDIT')")
    @Operation(summary = "Test API")
    @GetMapping("/hello")
    fun hello(): String {
        return "Hello, World!"
    }
}