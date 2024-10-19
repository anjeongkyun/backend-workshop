package org.backend.workshop.concurrency.control.dto

data class PlaceOrderCommand(
    val productId: Long,
    val quantity: Long,
)
