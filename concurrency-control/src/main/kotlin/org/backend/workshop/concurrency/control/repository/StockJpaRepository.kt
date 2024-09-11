package org.backend.workshop.concurrency.control.repository

import org.backend.workshop.concurrency.control.datamodel.StockDataModel
import org.springframework.data.jpa.repository.JpaRepository

interface StockJpaRepository : JpaRepository<StockDataModel, Long> {
    fun findByProductId(productId: Long): StockDataModel?
}
