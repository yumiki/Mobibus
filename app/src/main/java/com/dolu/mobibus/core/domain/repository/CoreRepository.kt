package com.dolu.mobibus.core.domain.repository

import com.dolu.mobibus.core.domain.model.BusTicket
import com.dolu.mobibus.core.domain.model.User
import com.dolu.mobibus.core.util.NetworkResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CoreRepository {
    suspend fun getBusTicketInfos(): Flow<NetworkResource<List<BusTicket>>>
    suspend fun getActiveUser(): StateFlow<User>
    suspend fun addBusTicketToCart(ticket: BusTicket): User
    suspend fun removeBusTicketFromCart(ticket: BusTicket): User
    suspend fun clearUserCart(): User
}