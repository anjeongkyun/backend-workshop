package org.flowcontroll.service

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

class Bucket4jRateLimiter : RateLimit {
    private val buckets: ConcurrentHashMap<String, Bucket> = ConcurrentHashMap()

    @Suppress("ktlint:standard:property-naming")
    private val TOKEN_NUMS = 1L

    @Suppress("ktlint:standard:property-naming")
    private val REQUEST_LIMIT = 5L

    @Suppress("ktlint:standard:property-naming")
    private val REFILL_INTERVAL = Duration.ofMinutes(1)

    @Suppress("ktlint:standard:property-naming")
    private val REFILL_TOKENS = 5L

    override fun isAllowed(key: String): Boolean {
        val bucket = resolveBucket(key)
        return bucket.tryConsume(TOKEN_NUMS)
    }

    private fun resolveBucket(key: String): Bucket =
        buckets.computeIfAbsent(key) {
            val limit = Bandwidth.classic(REQUEST_LIMIT, refill())
            Bucket.builder().addLimit(limit).build()
        }

    private fun refill(): Refill = Refill.greedy(REFILL_TOKENS, REFILL_INTERVAL)
}
