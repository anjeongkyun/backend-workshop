package org.backend.workshop.mongotransaction

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager

@Configuration
class MongoConfiguration(
    private val mongoDatabaseFactory: MongoDatabaseFactory
) {
    @Bean
    fun transactionManager(): MongoTransactionManager {
        return MongoTransactionManager(mongoDatabaseFactory)
    }
}
