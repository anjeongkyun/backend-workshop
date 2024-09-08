package org.flowcontroll.service

import org.springframework.data.redis.core.RedisTemplate
import java.time.Duration

class RedisRateLimiter(
    private val redisTemplate: RedisTemplate<String, String>,
) : RateLimit {
    @Suppress("ktlint:standard:property-naming")
    // 허용되는 최대 요청 수
    private val REQUEST_LIMIT = 5L

    @Suppress("ktlint:standard:property-naming")
    // 제한 시간 윈도우
    private val TIME_WINDOW = Duration.ofMinutes(1)

    @Suppress("ktlint:standard:property-naming")
    private val RATE_LIMIT_PREFIX = "RATE_LIMIT"

    /**
     * @param key 제한을 적용할 식별자 (예: 사용자 ID, IP 주소)
     * @return 요청 허용 여부
     */
    override fun isAllowed(key: String): Boolean {
        val rateLimitKey = "$RATE_LIMIT_PREFIX:$key"
        val current =
            redisTemplate.opsForValue().increment(rateLimitKey, 1) ?: 0L

        if (current == 1L) {
            redisTemplate.expire(rateLimitKey, TIME_WINDOW)
        }

        return current <= REQUEST_LIMIT
    }
}
