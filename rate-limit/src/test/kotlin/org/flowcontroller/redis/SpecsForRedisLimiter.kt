package org.flowcontroller.redis

import org.flowcontroll.service.RedisRateLimiter
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import java.time.Duration
import java.util.*

class SpecsForRedisLimiter {
    private lateinit var redisTemplate: RedisTemplate<String, String>
    private lateinit var valueOperations: ValueOperations<String, String>
    private lateinit var sut: RedisRateLimiter

    @BeforeEach
    fun setUp() {
        redisTemplate = mock(RedisTemplate::class.java) as RedisTemplate<String, String>
        valueOperations = mock(ValueOperations::class.java) as ValueOperations<String, String>
        sut = RedisRateLimiter(redisTemplate)

        `when`(redisTemplate.opsForValue()).thenReturn(valueOperations)
    }

    @Test
    fun `요청이 제한을 초과할 때 거부해야 한다`() {
        val key = UUID.randomUUID().toString()

        `when`(valueOperations.increment("RATE_LIMIT:$key", 1)).thenReturn(6L)

        val isAllowed = sut.isAllowed(key)

        assertFalse(isAllowed)
        verify(valueOperations).increment("RATE_LIMIT:$key", 1)
    }

    @Test
    fun `첫 번째 요청이 아닐 때 만료 시간을 설정하지 않아야 한다`() {
        val key = UUID.randomUUID().toString()

        `when`(valueOperations.increment("RATE_LIMIT:$key", 1)).thenReturn(2L)

        val isAllowed = sut.isAllowed(key)

        assertTrue(isAllowed)
        verify(redisTemplate, never()).expire("RATE_LIMIT:$key", Duration.ofMinutes(1))
    }
}
