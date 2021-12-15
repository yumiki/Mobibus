package com.dolu.mobibus.core.domain.model

import com.dolu.mobibus.R
import com.dolu.mobibus.core.data.local.entity.BusTicketEntity
import com.squareup.moshi.JsonClass
import java.lang.Exception
import java.text.DecimalFormat
import java.util.*

data class BusTicket(
    val id: String,
    val price: Price,
    val validityPeriod: Period,
) {
    fun getRessourceNameId(): Int {
        return when(validityPeriod) {
            Period.TRIP -> R.string.one_trip_ticket_name
            Period.DAY -> R.string.day_ticket_name
            Period.WEEK -> R.string.week_ticket_name
        }
    }

    fun toEntity(): BusTicketEntity {
        return BusTicketEntity(
            id,
            price,
            validityPeriod.name
        )
    }
}

class AlreadyConsumeException(): Exception()
class PassedException(): Exception()


@JsonClass(generateAdapter = true)
data class Price(
    val value: Double,
    val currencyCode: String
) {
    private fun getCurrency(): Currency {
        return Currency.getInstance(currencyCode)
    }

    override fun toString(): String {
        val formatter = DecimalFormat("0.00 '${getCurrency().symbol}'")
        return formatter.format(value)
    }
}

enum class Period(val count: Int){
    TRIP(1),
    DAY(1),
    WEEK(7)
}


val defaultTicketList: List<BusTicket> = listOf(
    BusTicket(
        "one_way",
        Price(1.1, "EUR"),
        Period.TRIP
    ),
    BusTicket(
        "one_day",
        Price(2.5, "EUR"),
        Period.DAY
    ),
    BusTicket(
        "one_week",
        Price(12.0, "EUR"),
        Period.WEEK
    )
)