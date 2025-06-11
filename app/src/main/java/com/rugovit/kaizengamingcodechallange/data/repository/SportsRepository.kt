package com.rugovit.kaizengamingcodechallange.data.repository

import arrow.core.Either
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportWithEventsPOJO
import kotlinx.coroutines.flow.Flow

interface SportsRepository {
    fun getSportsWithEvents(): Flow<List<SportWithEventsPOJO>>
    suspend fun syncSportsData(): Either<AppError, List<SportWithEventsPOJO>>
    suspend fun toggleFavorite(eventId: String) : Either<AppError, Unit>
}