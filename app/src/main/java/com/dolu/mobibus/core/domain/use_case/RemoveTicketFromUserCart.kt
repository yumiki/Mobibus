package com.dolu.mobibus.core.domain.use_case

import com.dolu.mobibus.core.domain.model.BusTicket
import com.dolu.mobibus.core.domain.model.User
import com.dolu.mobibus.core.domain.repository.CoreRepository

class RemoveTicketFromUserCart(
    private val repository: CoreRepository
) {
    suspend operator fun invoke(ticket: BusTicket): User {
        return repository.removeBusTicketFromCart(ticket)
    }
}