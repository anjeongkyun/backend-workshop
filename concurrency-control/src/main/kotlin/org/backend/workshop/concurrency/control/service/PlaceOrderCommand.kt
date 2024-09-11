package org.backend.workshop.concurrency.control.service

data class PlaceOrderCommand(
    val productId: Long,
    val quantity: Int,
)
