package org.flowcontroll.controller

import org.flowcontroll.service.Bucket4jRateLimiter
import org.flowcontroll.service.RedisRateLimiter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class RateLimitTestController(
    private val redisRateLimiter: RedisRateLimiter,
    private val bucket4jRateLimiter: Bucket4jRateLimiter,
) {
    @PostMapping("/redis/rate-limit-test")
    fun rateLimitTest(
        @RequestHeader(value = "X-Forwarded-For", required = false) ip: String?,
    ): ResponseEntity<String> {
        val clientIp = ip ?: "unknown"

        return if (redisRateLimiter.isAllowed(clientIp)) {
            ResponseEntity.ok("OK")
        } else {
            ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests. Please try again later.")
        }
    }

    @PostMapping("/bucket4j/rate-limit-test")
    fun getData(
        @RequestHeader(value = "X-Forwarded-For", required = false) ip: String?,
    ): ResponseEntity<String> {
        val clientIp = ip ?: "unknown"
        return if (bucket4jRateLimiter.isAllowed(clientIp)) {
            ResponseEntity.ok("OK")
        } else {
            ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests. Please try again later.")
        }
    }
}
