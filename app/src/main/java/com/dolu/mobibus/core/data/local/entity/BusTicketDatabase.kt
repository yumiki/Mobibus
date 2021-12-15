package com.dolu.mobibus.core.data.local.entity

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dolu.mobibus.core.data.local.BusTicketDao
import com.dolu.mobibus.core.data.local.Converters

@Database(
    entities = [BusTicketEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class BusTicketDatabase: RoomDatabase() {
    abstract val dao: BusTicketDao
}