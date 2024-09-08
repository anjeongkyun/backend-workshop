package org.flowcontroll.service

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

class Bucket4jRateLimiter : RateLimit {
    private val buckets: ConcurrentHashMap<String, Bucket> = ConcurrentHashMap()

    override fun isAllowed(key: String): Boolean {
        val bucket = resolveBucket(key)
        return bucket.tryConsume(1)
    }

    private fun resolveBucket(key: String): Bucket =
        buckets.computeIfAbsent(key) {
            // 1분 동안 5번의 요청을 허용하는 Rate Limiting 설정
            val limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)))
            Bucket.builder().addLimit(limit).build()
        }
}
