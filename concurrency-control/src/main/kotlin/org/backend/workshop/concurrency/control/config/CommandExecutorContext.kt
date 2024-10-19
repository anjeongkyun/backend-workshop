package org.backend.workshop.concurrency.control.config

import org.backend.workshop.concurrency.control.repository.OrderJpaRepository
import org.backend.workshop.concurrency.control.repository.ProductJpaRepository
import org.backend.workshop.concurrency.control.service.PlaceOrderCommandExecutor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommandExecutorContext {
    @Bean
    fun placeOrderCommandExecutor(
        productJpaRepository: ProductJpaRepository,
        orderJpaRepository: OrderJpaRepository,
    ): PlaceOrderCommandExecutor =
        PlaceOrderCommandExecutor(
            orderJpaRepository = orderJpaRepository,
            productJpaRepository = productJpaRepository,
        )
}
