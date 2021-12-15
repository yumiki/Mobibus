package com.dolu.mobibus.core.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.dolu.mobibus.core.data.local.util.JsonParser
import com.dolu.mobibus.core.domain.model.BusTicket
import com.dolu.mobibus.core.domain.model.Price
import com.dolu.mobibus.core.domain.model.Transactions
import com.squareup.moshi.Types

/**
 * Convert complex data to json and vice versa
 */
@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromPriceJson(json: String): Price? {
        return jsonParser.fromJson<Price>(json, Price::class.java)
    }

    @TypeConverter
    fun toPriceJson(price: Price): String? {
        return jsonParser.toJson(price, Price::class.java)
    }

    @TypeConverter
    fun fromBusTicketListToJson(json: String): List<BusTicket> {
        return jsonParser.fromJson(
            json,
            Types.newParameterizedType(List::class.java, BusTicket::class.java)
        ) ?: emptyList()
    }

    @TypeConverter
    fun toBusTicketListfromJson(tickets: List<BusTicket>): String {
        return jsonParser.toJson(
            tickets,
            Types.newParameterizedType(List::class.java, BusTicket::class.java)
        ) ?: "[]"
    }

    @TypeConverter
    fun fromTransactionListToJson(json: String): List<Transactions> {
        return jsonParser.fromJson(
            json,
            Types.newParameterizedType(List::class.java, Transactions::class.java)
        ) ?: emptyList()
    }

    @TypeConverter
    fun toTransactionsListfromJson(transactions: List<Transactions>): String {
        return jsonParser.toJson(
            transactions,
            Types.newParameterizedType(List::class.java, Transactions::class.java)
        ) ?: "[]"
    }
}