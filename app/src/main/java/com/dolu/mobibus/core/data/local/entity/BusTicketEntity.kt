package com.dolu.mobibus.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dolu.mobibus.core.domain.model.BusTicket
import com.dolu.mobibus.core.domain.model.Period
import com.dolu.mobibus.core.domain.model.Price

@Entity
data class BusTicketEntity(
    @PrimaryKey val id: String,
    val price: Price,
    val validityPeriod: String,
    val startConsumptionTimestamp: Long? = null
) {
    fun toBusTicket(): BusTicket {
        return BusTicket(
            id,
            price,
            Period.valueOf(validityPeriod)
        )
    }
}