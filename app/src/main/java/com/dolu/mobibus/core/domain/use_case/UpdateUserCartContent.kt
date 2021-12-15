package com.dolu.mobibus.core.domain.use_case

import com.dolu.mobibus.core.domain.model.BusTicket
import com.dolu.mobibus.core.domain.repository.CoreRepository

data class UpdateUserCartContent(
    val addTicket: AddTicketToUserCart,
    val removeTicket: RemoveTicketFromUserCart
)