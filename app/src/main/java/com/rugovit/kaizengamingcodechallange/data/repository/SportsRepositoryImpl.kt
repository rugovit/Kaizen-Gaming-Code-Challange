package com.rugovit.kaizengamingcodechallange.data.repository

import arrow.core.Either
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.core.common.TransactionRunner
import com.rugovit.kaizengamingcodechallange.data.database.dao.SportDao
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportWithEventsPOJO
import com.rugovit.kaizengamingcodechallange.data.mapper.toEntity
import com.rugovit.kaizengamingcodechallange.data.network.ApiService
import kotlinx.coroutines.flow.Flow

class SportsRepositoryImpl(
    private val tx: TransactionRunner,
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
            //add logic  to remove events that are not in the new data as this is test project,
            // on real  production app   ww would do this in a more sophisticated way such as using a diffing algorithm or a more complex sync strategy

            val newEventIds = eventsEntities.map { it.eventId }.toSet()
            val newSportIds = sportsEntities.map { it.sportId }.toSet()

            val staleEvents = sportDao.getAllEvents().filterNot { it.eventId in newEventIds }
            val staleSports = sportDao.getAllSports().filterNot { it.sportId in newSportIds }

            // Delete events that are not in the new data
            tx.run {
                // Remove events not in new data
                if (staleEvents.isNotEmpty()) {
                    sportDao.deleteEvents(staleEvents.map { it.eventId })
                }
                // Remove sports not in new data (cascade deletes their events)
                if (staleSports.isNotEmpty()) {
                    sportDao.deleteSports(staleSports.map { it.sportId })
                }
                // Insert or update fresh data
                sportDao.insertSports(sportsEntities)
                sportDao.insertEvents(eventsEntities)
            }
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