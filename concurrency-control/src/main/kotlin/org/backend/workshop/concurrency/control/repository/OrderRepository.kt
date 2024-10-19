package org.backend.workshop.concurrency.control.repository

import org.backend.workshop.concurrency.control.datamodel.OrderDataModel

interface OrderRepository {
    fun save(
        id: String,
        modifier: (OrderDataModel) -> OrderDataModel,
    ): OrderDataModel
}
