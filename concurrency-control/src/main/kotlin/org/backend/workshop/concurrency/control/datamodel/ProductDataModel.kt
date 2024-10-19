@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.backend.workshop.concurrency.control.datamodel

import jakarta.persistence.*

@Entity
@Table(name = "products")
data class ProductDataModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    val price: Double,
    @Column(nullable = false)
    val quantity: Long,
) {
    fun decreaseQuantity(quantity: Long): ProductDataModel {
        require(quantity > 0) { "Quantity must be greater than 0" }
        require(this.quantity >= quantity) { "Not enough quantity" }
        return this.copy(quantity = this.quantity - quantity)
    }

    fun toModel(): ProductDataModel =
        ProductDataModel(
            name = this.name,
            price = this.price,
            quantity = this.quantity,
        )
}
