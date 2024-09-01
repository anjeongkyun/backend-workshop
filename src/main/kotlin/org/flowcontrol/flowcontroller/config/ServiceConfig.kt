package org.flowcontrol.flowcontroller.config

import org.flowcontrol.flowcontroller.service.ratelimit.RateLimiter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class ServiceConfig {
    @Bean
    fun rateLimiter(redisTemplate: RedisTemplate<String, String>): RateLimiter = RateLimiter(redisTemplate)
}
