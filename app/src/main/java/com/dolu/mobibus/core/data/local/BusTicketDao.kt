package com.dolu.mobibus.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dolu.mobibus.core.data.local.entity.BusTicketEntity

@Dao
interface BusTicketDao {
    @Query("SELECT * FROM busticketentity")
    fun getAllBusTicketInfos(): List<BusTicketEntity>

    @Query("DELETE FROM busticketentity WHERE id IN(:busTicketInfoIds)")
    fun deleteBusTicketInfos(busTicketInfoIds: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBusTicketInfos(infos: List<BusTicketEntity>)
}
