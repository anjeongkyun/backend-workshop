package org.backend.workshop.concurrency.control

import autoparams.AutoSource
import org.backend.workshop.concurrency.control.config.DataSourceConfig
import org.backend.workshop.concurrency.control.datamodel.ProductDataModel
import org.backend.workshop.concurrency.control.dto.PlaceOrderCommand
import org.backend.workshop.concurrency.control.repository.OrderJpaRepository
import org.backend.workshop.concurrency.control.repository.ProductJpaRepository
import org.backend.workshop.concurrency.control.service.PlaceOrderCommandExecutor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@DataJpaTest
@ContextConfiguration(classes = [DataSourceConfig::class])
class PlaceOrderCommandExecutorTest {
    @Autowired
    private lateinit var orderJpaRepository: OrderJpaRepository

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @AutoSource
    @ParameterizedTest
    fun `should place orders concurrently`(product: ProductDataModel) {
        val saved = productJpaRepository.saveAndFlush(product.copy(id = null, quantity = 100L))
        val a =
            productJpaRepository
                .findById(saved.id!!)

        val sut =
            PlaceOrderCommandExecutor(
                orderJpaRepository = orderJpaRepository,
                productJpaRepository = productJpaRepository,
            )
        val command = PlaceOrderCommand(productId = saved.id!!, quantity = 1L)

        val numberOfThreads = 10
        val latch = CountDownLatch(numberOfThreads)
        val executorService = Executors.newFixedThreadPool(numberOfThreads)

        for (i in 0 until numberOfThreads) {
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

        val decreasedProduct = productJpaRepository.findById(saved.id!!).get()
        assertEquals(90, decreasedProduct.quantity)
    }
}
