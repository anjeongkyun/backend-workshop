@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.backend.workshop.concurrency.control.datamodel

import jakarta.persistence.*
import org.backend.workshop.concurrency.control.service.PlaceOrderCommand

@Entity
@Table(name = "orders")
data class OrderDataModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: ProductDataModel,
    @Column(nullable = false)
    val quantity: Int,
    @Column(nullable = false)
    val totalPrice: Double,
) {
    companion object {
        fun placeOrder(
            command: PlaceOrderCommand,
            product: ProductDataModel,
        ): OrderDataModel =
            OrderDataModel(
                product = product,
                quantity = command.quantity,
                totalPrice = product.price * command.quantity,
            )
    }
}
