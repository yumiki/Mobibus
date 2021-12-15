package com.dolu.mobibus.core.domain.use_case

import com.dolu.mobibus.core.domain.model.BusTicket
import com.dolu.mobibus.core.domain.repository.CoreRepository
import com.dolu.mobibus.core.util.NetworkResource
import kotlinx.coroutines.flow.Flow

class GetBusTicketInfos(
    private val repository: CoreRepository
) {
    suspend operator fun invoke(): Flow<NetworkResource<List<BusTicket>>> {
        return repository.getBusTicketInfos()
    }
}