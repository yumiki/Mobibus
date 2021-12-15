package com.dolu.mobibus.core.ui.home

import com.dolu.mobibus.core.domain.model.BusTicket

data class BusTicketInfoState(
        val busTicketInfoItems: List<BusTicket> = emptyList(),
        val isLoading: Boolean = false
)
