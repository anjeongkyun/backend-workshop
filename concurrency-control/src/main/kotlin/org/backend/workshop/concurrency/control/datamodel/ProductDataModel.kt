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
    @OneToOne(mappedBy = "product")
    val stock: StockDataModel? = null,
) {
    fun toModel(): ProductDataModel =
        ProductDataModel(
            name = this.name,
            price = this.price,
            stock = this.stock?.toModel(),
        )
}
