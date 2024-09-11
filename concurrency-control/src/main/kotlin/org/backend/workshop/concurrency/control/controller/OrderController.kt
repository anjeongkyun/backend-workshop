package org.backend.workshop.concurrency.control.controller

import org.backend.workshop.concurrency.control.service.PlaceOrderCommand
import org.backend.workshop.concurrency.control.service.PlaceOrderCommandExecutor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(
    private val placeOrderCommandExecutor: PlaceOrderCommandExecutor,
) {
    @PostMapping("/order")
    fun placeOrder(command: PlaceOrderCommand) {
        placeOrderCommandExecutor.execute(command)
    }
}
