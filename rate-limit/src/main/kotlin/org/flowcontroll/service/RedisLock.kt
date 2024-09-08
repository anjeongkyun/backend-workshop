package org.flowcontroll.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisLock(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    fun acquireLock(
        lockKey: String,
        timeout: Long,
    ): Boolean = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", timeout, TimeUnit.MILLISECONDS) == true

    fun releaseLock(lockKey: String) {
        redisTemplate.delete(lockKey)
    }
}
