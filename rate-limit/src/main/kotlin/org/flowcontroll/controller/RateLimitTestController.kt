package org.flowcontroll.controller

import org.flowcontroll.service.RateLimiter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class RateLimitTestController(
    private val rateLimiter: RateLimiter,
) {
    @PostMapping("/rate-limit-test")
    fun rateLimitTest(
        @RequestHeader(value = "X-Forwarded-For", required = false) ip: String?,
    ): ResponseEntity<String> {
        val clientIp = ip ?: "unknown"

        return if (rateLimiter.isAllowed(clientIp)) {
            ResponseEntity.ok("OK")
        } else {
            ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests. Please try again later.")
        }
    }
}
