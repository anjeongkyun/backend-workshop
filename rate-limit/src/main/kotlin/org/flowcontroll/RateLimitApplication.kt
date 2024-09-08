package org.flowcontroll

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RateLimitControllerApplication

fun main(args: Array<String>) {
    runApplication<RateLimitControllerApplication>(*args)
}
