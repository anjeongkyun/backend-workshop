@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.backend.workshop.concurrency.control.datamodel

import jakarta.persistence.*

@Entity
@Table(name = "stocks")
data class StockDataModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    var quantity: Int,
    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: ProductDataModel,
) {
    fun decreaseStock(quantity: Int): StockDataModel {
        if (this.quantity < quantity) {
            throw IllegalArgumentException("Stock is not enough")
        }
        this.quantity -= quantity
        return this
    }

    fun toModel(): StockDataModel =
        StockDataModel(
            quantity = this.quantity,
            product = this.product.toModel(),
        )
}
