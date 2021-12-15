package com.dolu.mobibus.core.domain.use_case

data class UpdateUserCartContent(
    val addTicket: AddTicketToUserCart,
    val removeTicket: RemoveTicketFromUserCart,
    val clearUserCart: ClearUserCart
)