package org.backend.workshop.mongotransaction

import org.springframework.data.mongodb.repository.MongoRepository

interface PaymentMongoRepository : MongoRepository<PaymentEventStore, String> {
}
