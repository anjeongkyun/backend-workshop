package org.backend.workshop.mongotransaction.services

import org.assertj.core.api.Assertions
import org.backend.workshop.mongotransaction.EventStoreService
import org.backend.workshop.mongotransaction.PaymentMongoRepository
import org.backend.workshop.mongotransaction.ReservationMongoRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import kotlin.test.BeforeTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventStoreServiceTest {

    @Autowired
    lateinit var paymentMongoRepository: PaymentMongoRepository

    @Autowired
    lateinit var reservationMongoRepository: ReservationMongoRepository

    @Autowired
    lateinit var eventStoreService: EventStoreService

    companion object {
        private val mongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:7.0.9"))
            .apply {
                setCommand("--replSet", "rs0", "--bind_ip_all")
                start()
            }

        @JvmStatic
        @DynamicPropertySource
        fun mongoProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl)
        }
    }

    @BeforeTest
    fun cleanup() {
        paymentMongoRepository.deleteAll()
        reservationMongoRepository.deleteAll()
    }

    @Test
    fun `트랜잭션 롤백 검증`() {
        Assertions.assertThatThrownBy {
            eventStoreService.savePaymentEvent(true)
        }
        val payments = paymentMongoRepository.findAll()
        val reservations = reservationMongoRepository.findAll()
        Assertions.assertThat(payments).isEmpty()
        Assertions.assertThat(reservations).isEmpty()
    }

    @Test
    fun `정상동작 검증`() {
        val service = EventStoreService(paymentMongoRepository, reservationMongoRepository)
        service.savePaymentEvent(false)
        val actual = paymentMongoRepository.findAll()
        Assertions.assertThat(actual).hasSize(1)
        Assertions.assertThat(reservationMongoRepository.findAll()).hasSize(1)
    }
}