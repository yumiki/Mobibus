package com.dolu.mobibus.core.domain.use_case

import com.dolu.mobibus.core.domain.model.BusTicket
import com.dolu.mobibus.core.domain.model.User
import com.dolu.mobibus.core.domain.repository.CoreRepository
import kotlinx.coroutines.flow.Flow

class AddTicketToUserCart(
    private val repository: CoreRepository
) {
    suspend operator fun invoke(ticket: BusTicket): User {
        return repository.addBusTicketToCart(ticket)
    }
}