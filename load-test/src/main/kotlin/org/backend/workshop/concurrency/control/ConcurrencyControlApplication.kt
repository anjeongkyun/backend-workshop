package org.backend.workshop.concurrency.control

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConcurrencyControlApplication

fun main(args: Array<String>) {
    runApplication<ConcurrencyControlApplication>(*args)
}
