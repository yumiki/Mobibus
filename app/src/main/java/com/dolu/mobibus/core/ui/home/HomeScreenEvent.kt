package com.dolu.mobibus.core.ui.home

import com.dolu.mobibus.core.domain.model.BusTicket

sealed class HomeScreenEvent{
    data class AddTicketToCart(val ticket: BusTicket): HomeScreenEvent()
    data class RemoveTicketToCart(val ticket: BusTicket): HomeScreenEvent()
    object ValidateOrder: HomeScreenEvent()
}