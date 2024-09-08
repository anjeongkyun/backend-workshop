package org.flowcontroll.config

import org.flowcontroll.service.RateLimiter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class ServiceConfig {
    @Bean
    fun rateLimiter(redisTemplate: RedisTemplate<String, String>): RateLimiter = RateLimiter(redisTemplate)
}
