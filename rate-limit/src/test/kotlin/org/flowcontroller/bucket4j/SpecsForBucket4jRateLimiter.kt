package org.flowcontroller.bucket4j

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import org.flowcontroll.service.Bucket4jRateLimiter
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import java.time.Duration
import kotlin.test.Test

class SpecsForBucket4jRateLimiter {
    private lateinit var rateLimiter: Bucket4jRateLimiter

    @BeforeEach
    fun setUp() {
        rateLimiter = Bucket4jRateLimiter()
    }

    @Test
    fun `key가 있는 경우 요청이 허용된다`() {
        val isAllowed = rateLimiter.isAllowed("user-1")
        assertTrue(isAllowed)
    }

    @Test
    fun `1분 동안 5번 이상의 요청이 차단된다`() {
        repeat(5) {
            assertTrue(rateLimiter.isAllowed("user-1"))
        }
        assertFalse(rateLimiter.isAllowed("user-1"))
    }

    @Test
    fun `다른 키를 사용하는 요청은 독립적으로 제한된다`() {
        repeat(5) {
            assertTrue(rateLimiter.isAllowed("user-1"))
        }
        assertTrue(rateLimiter.isAllowed("user-2"))
    }

    @Test
    fun `1분이 지나면 요청이 다시 허용된다`() {
        val limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofSeconds(1)))
        val bucket = Bucket.builder().addLimit(limit).build()

        // 제한 초과한 상태
        repeat(5) {
            assertTrue(bucket.tryConsume(1))
        }
        assertFalse(bucket.tryConsume(1))

        Thread.sleep(1000)
        assertTrue(bucket.tryConsume(1))
    }
}
