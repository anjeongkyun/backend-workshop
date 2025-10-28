package org.backend.workshop.concurrency.control

import org.backend.workshop.concurrency.control.datamodel.ProductDataModel
import org.backend.workshop.concurrency.control.dto.PlaceOrderCommand
import org.backend.workshop.concurrency.control.repository.OrderJpaRepository
import org.backend.workshop.concurrency.control.repository.ProductJpaRepository
import org.backend.workshop.concurrency.control.service.PlaceOrderCommandExecutor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class PlaceOrderCommandExecutorTest {
    @Autowired
    private lateinit var orderJpaRepository: OrderJpaRepository

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @Autowired
    private lateinit var placeOrderCommandExecutor: PlaceOrderCommandExecutor

    @Test
    fun `should place orders concurrently`() {
        val product = ProductDataModel(
            id = null,
            name = "Test Product",
            price = 10.0,
            quantity = 100L
        )
        val saved = productJpaRepository.saveAndFlush(product)
        val sut = placeOrderCommandExecutor
        val command = PlaceOrderCommand(productId = saved.id!!, quantity = 1L)

        val numberOfThreads = 10
        val latch = CountDownLatch(numberOfThreads)
        val executorService = Executors.newFixedThreadPool(numberOfThreads)

        repeat(numberOfThreads) {
            executorService.submit {
                try {
                    sut.execute(command)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executorService.shutdown()

        val placedOrders = orderJpaRepository.findAll()
        assertEquals(10, placedOrders.size)

        val decreasedProduct = productJpaRepository.findById(saved.id).get()
        assertEquals(90L, decreasedProduct.quantity)
    }
}
