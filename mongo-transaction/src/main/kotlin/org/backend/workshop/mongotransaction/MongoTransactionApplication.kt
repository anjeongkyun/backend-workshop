package org.backend.workshop.mongotransaction

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MongoTransactionApplication

fun main(args: Array<String>) {
    runApplication<MongoTransactionApplication>(*args)
}
