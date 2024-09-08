package org.flowcontroll.service

interface RateLimit {
    fun isAllowed(key: String): Boolean
}
