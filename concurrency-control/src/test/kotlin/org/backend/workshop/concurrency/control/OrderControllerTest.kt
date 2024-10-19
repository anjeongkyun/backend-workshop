package org.backend.workshop.concurrency.control

import kotlinx.coroutines.*
import org.backend.workshop.concurrency.control.dto.PlaceOrderCommand
import org.backend.workshop.concurrency.control.repository.OrderJpaRepository
import org.backend.workshop.concurrency.control.repository.ProductJpaRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class OrderControllerTest {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @Autowired
    private lateinit var orderJpaRepository: OrderJpaRepository

    @Test
    fun `should place orders concurrently`() =
        runBlocking {
            val numberOfCoroutines = 10
            val productId = 1L // 테스트할 상품 ID
            val quantity = 1L // 주문 수량

            // 코루틴을 사용하여 병렬로 요청을 보냄
            val jobs = mutableListOf<Job>()
            repeat(numberOfCoroutines) {
                jobs.add(
                    launch(Dispatchers.IO) {
                        webTestClient
                            .post()
                            .uri("/order")
                            .bodyValue(PlaceOrderCommand(productId, quantity))
                            .exchange()
                            .expectStatus()
                            .isOk
                    },
                )
            }

            jobs.joinAll()

            val orders = orderJpaRepository.findAll()
            assertEquals(10, orders.size)

            val products = productJpaRepository.findAll().first()
            assertEquals(90, products.quantity)
        }
}
