package org.flowcontroll.config

import org.flowcontroll.service.Bucket4jRateLimiter
import org.flowcontroll.service.RedisRateLimiter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class ServiceConfig {
    @Bean
    fun rateLimiter(redisTemplate: RedisTemplate<String, String>): RedisRateLimiter = RedisRateLimiter(redisTemplate)

    @Bean
    fun bucket4jRateLimiter(): Bucket4jRateLimiter = Bucket4jRateLimiter()
}
