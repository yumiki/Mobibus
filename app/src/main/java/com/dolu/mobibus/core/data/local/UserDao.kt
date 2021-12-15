package com.dolu.mobibus.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dolu.mobibus.core.data.local.entity.BusTicketEntity
import com.dolu.mobibus.core.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

    @Query("DELETE FROM userentity WHERE id = :id")
    fun deleteUser(id: String)

    @Query("SELECT * FROM userentity WHERE id = :id")
    fun getUserById(id: String): UserEntity?
}
