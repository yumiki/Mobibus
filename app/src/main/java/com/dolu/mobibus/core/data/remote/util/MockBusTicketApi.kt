package com.dolu.mobibus.core.data.remote.util

import com.dolu.mobibus.core.data.remote.BusTicketApi
import com.dolu.mobibus.core.domain.model.BusTicket
import com.dolu.mobibus.core.domain.model.defaultTicketList

class MockBusTicketApi: BusTicketApi {
    override suspend fun getBusTicketInfos(): List<BusTicket> {
        return defaultTicketList
    }
}