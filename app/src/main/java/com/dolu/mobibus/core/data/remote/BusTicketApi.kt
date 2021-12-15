package com.dolu.mobibus.core.data.remote

import com.dolu.mobibus.core.domain.model.BusTicket
import retrofit2.http.GET

interface BusTicketApi {
    @GET("/products/details")
    suspend fun getBusTicketInfos(): List<BusTicket>

    companion object {
        const val BASE_URL = "https://unknown.com" //TODO change this
    }
}