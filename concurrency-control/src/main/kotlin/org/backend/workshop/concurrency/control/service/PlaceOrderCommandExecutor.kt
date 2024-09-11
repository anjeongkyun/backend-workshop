package org.backend.workshop.concurrency.control.service

import jakarta.transaction.Transactional
import org.backend.workshop.concurrency.control.datamodel.OrderDataModel
import org.backend.workshop.concurrency.control.repository.OrderJpaRepository
import org.backend.workshop.concurrency.control.repository.ProductJpaRepository
import org.backend.workshop.concurrency.control.repository.StockJpaRepository

@Transactional
open class PlaceOrderCommandExecutor(
    private val orderJpaRepository: OrderJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
    private val stockJpaRepository: StockJpaRepository,
) {
    fun execute(command: PlaceOrderCommand) {
        val productDataModel =
            productJpaRepository.findById(command.productId).orElseThrow { RuntimeException("Product not found") }

        val stock =
            stockJpaRepository.findByProductId(command.productId)
                ?: throw RuntimeException("Stock not found")

        stock.decreaseStock(command.quantity)
        orderJpaRepository.save(OrderDataModel.placeOrder(command, productDataModel))
    }
}
