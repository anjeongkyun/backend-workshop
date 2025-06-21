package org.backend.workshop.mongotransaction

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document(collection = "payment_events")
data class PaymentEventStore(
    @Id
    val id: String? = null,
    val correlationId: String,
    val payload: String,
    val createdDateTime: Date = Date(),
)
