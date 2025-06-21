package org.backend.workshop.mongotransaction

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class EventStoreService(
    private val paymentMongoRepository: PaymentMongoRepository,
    private val reservationMongoRepository: ReservationMongoRepository,
) {

    @Transactional
    fun savePaymentEvent(isFail: Boolean) {
        val correlationId = UUID.randomUUID().toString()
        paymentMongoRepository.save(
            PaymentEventStore(
                correlationId = correlationId,
                payload = "Payment event payload",
            )
        )
        reservationMongoRepository.save(
            ReservationEventStore(
                correlationId = correlationId,
                payload = "Reservation event payload",
            )
        )

        if (isFail) {
            throw RuntimeException("Simulated exception to test transaction rollback")
        }
    }
}
