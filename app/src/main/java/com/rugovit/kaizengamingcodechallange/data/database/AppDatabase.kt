package com.rugovit.kaizengamingcodechallange.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rugovit.kaizengamingcodechallange.data.database.dao.SportDao
import com.rugovit.kaizengamingcodechallange.data.database.entities.EventEntity
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportEntity

@Database(entities = [SportEntity::class, EventEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sportDao(): SportDao
}