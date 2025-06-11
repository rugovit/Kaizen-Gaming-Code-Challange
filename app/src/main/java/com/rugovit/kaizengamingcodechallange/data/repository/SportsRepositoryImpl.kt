package com.rugovit.kaizengamingcodechallange.data.repository

import androidx.room.withTransaction
import arrow.core.Either
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.data.database.AppDatabase
import com.rugovit.kaizengamingcodechallange.data.database.dao.SportDao
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportWithEventsPOJO
import com.rugovit.kaizengamingcodechallange.data.mapper.toEntity
import com.rugovit.kaizengamingcodechallange.data.network.ApiService
import kotlinx.coroutines.flow.Flow

class SportsRepositoryImpl(
    private val database: AppDatabase,
    private val sportDao: SportDao,
    private val apiService: ApiService
) : SportsRepository {

    override fun getSportsWithEvents(): Flow<List<SportWithEventsPOJO>> = sportDao.getSportsWithEventsStream()

    override suspend fun syncSportsData(): Either<AppError,List<SportWithEventsPOJO>> {
        return try {
            // Fetch sports data from the API
            val sportsNetwork = apiService.getSports()

            // Get existing events to preserve favorite status
            val existingEvents = sportDao.getAllEvents().associateBy { it.eventId }

            // Map network models to entities
            val sportsEntities = sportsNetwork.map { it.toEntity() }
            val eventsEntities = sportsNetwork.flatMap { sport ->
                sport.e.map { eventNetwork ->
                    val existingEvent = existingEvents[eventNetwork.i]
                    eventNetwork.toEntity(existingEvent?.isFavorite ?: false)
                }
            }

            // Insert data into the database within a transaction
            database.withTransaction {
                sportDao.insertSports(sportsEntities)
                sportDao.insertEvents(eventsEntities)
            }

            // return success
            return Either.Right(sportDao.getSportsWithEvents())
        }
        catch (e: Exception) {
            Either.Left(AppError.fromException(e))
        }

    }

    override suspend fun toggleFavorite(eventId: String) : Either<AppError, Unit> {
        return try {
            sportDao.toggleFavorite(eventId)
            return Either.Right(Unit)
        }
        catch (e: Exception) {
            Either.Left(AppError.fromException(e))
        }
    }
}