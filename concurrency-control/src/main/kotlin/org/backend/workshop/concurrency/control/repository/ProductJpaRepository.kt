@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.backend.workshop.concurrency.control.repository

import jakarta.persistence.LockModeType
import jakarta.persistence.QueryHint
import org.backend.workshop.concurrency.control.datamodel.ProductDataModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import java.util.*

interface ProductJpaRepository : JpaRepository<ProductDataModel, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000"))
    @Query("SELECT p FROM ProductDataModel p WHERE p.id = :id")
    fun findProductByIdWithLock(id: Long): Optional<ProductDataModel>
}
