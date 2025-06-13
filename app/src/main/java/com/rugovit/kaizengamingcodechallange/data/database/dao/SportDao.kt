package com.rugovit.kaizengamingcodechallange.data.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rugovit.kaizengamingcodechallange.data.database.entities.EventEntity
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportEntity
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportWithEventsPOJO
import kotlinx.coroutines.flow.Flow

@Dao
interface SportDao {
    @Transaction
    @Query("SELECT * FROM sports")
    fun getSportsWithEventsStream(): Flow<List<SportWithEventsPOJO>>

    @Transaction
    @Query("SELECT * FROM sports")
    suspend fun getSportsWithEvents(): List<SportWithEventsPOJO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSports(sports: List<SportEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("SELECT * FROM events")
    suspend fun getAllEvents(): List<EventEntity>

    @Query("SELECT * FROM sports")
    suspend fun getAllSports(): List<SportEntity>

    @Query("UPDATE events SET isFavorite = NOT isFavorite WHERE eventId = :eventId")
    suspend fun toggleFavorite(eventId: String)

    @Query("DELETE FROM events WHERE eventId IN (:eventsIds)")
    suspend fun deleteEvents(eventsIds: List<String>)

    @Query("DELETE FROM sports WHERE sportId IN (:sportsIds)")
    suspend fun deleteSports(sportsIds: List<String>)
}

