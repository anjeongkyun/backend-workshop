package org.backend.workshop.concurrency.control.service

import org.backend.workshop.concurrency.control.datamodel.OrderDataModel
import org.backend.workshop.concurrency.control.dto.PlaceOrderCommand
import org.backend.workshop.concurrency.control.repository.OrderJpaRepository
import org.backend.workshop.concurrency.control.repository.ProductJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlaceOrderCommandExecutor(
    private val orderJpaRepository: OrderJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
) {
    @Transactional
    fun execute(command: PlaceOrderCommand) {
        val productDataModel =
            productJpaRepository
                .findProductByIdWithLock(command.productId)
                .orElseThrow { RuntimeException("Product not found") }

        val decreased = productDataModel.decreaseQuantity(command.quantity)
        productJpaRepository.save(decreased)
        orderJpaRepository.save(OrderDataModel.placeOrder(command, decreased))
    }
}
