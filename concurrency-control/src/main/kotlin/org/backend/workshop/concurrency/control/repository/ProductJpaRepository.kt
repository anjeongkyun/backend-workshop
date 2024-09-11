package org.backend.workshop.concurrency.control.repository

import org.backend.workshop.concurrency.control.datamodel.ProductDataModel
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<ProductDataModel, Long>
