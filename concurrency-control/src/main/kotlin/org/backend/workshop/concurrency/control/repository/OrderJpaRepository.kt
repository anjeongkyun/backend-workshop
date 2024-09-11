package org.backend.workshop.concurrency.control.repository

import org.backend.workshop.concurrency.control.datamodel.OrderDataModel
import org.springframework.data.jpa.repository.JpaRepository

interface OrderJpaRepository : JpaRepository<OrderDataModel, Long>
