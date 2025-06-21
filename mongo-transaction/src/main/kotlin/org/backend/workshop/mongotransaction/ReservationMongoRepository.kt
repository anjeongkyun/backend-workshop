package org.backend.workshop.mongotransaction

import org.springframework.data.mongodb.repository.MongoRepository

interface ReservationMongoRepository : MongoRepository<ReservationEventStore, String> {
}
