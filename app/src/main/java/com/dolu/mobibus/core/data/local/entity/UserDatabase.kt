package com.dolu.mobibus.core.data.local.entity

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dolu.mobibus.core.data.local.Converters
import com.dolu.mobibus.core.data.local.UserDao

@Database(
    entities = [UserEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class UserDatabase: RoomDatabase() {
    abstract val dao: UserDao
}