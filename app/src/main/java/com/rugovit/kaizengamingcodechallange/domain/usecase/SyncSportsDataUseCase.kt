package com.rugovit.kaizengamingcodechallange.domain.usecase

import arrow.core.Either
import arrow.core.flatMap
import com.rugovit.kaizengamingcodechallange.core.base.domain.SuspendUseCase
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportWithEventsPOJO
import com.rugovit.kaizengamingcodechallange.data.repository.SportsRepository
import com.rugovit.kaizengamingcodechallange.domain.mapper.toDomain
import com.rugovit.kaizengamingcodechallange.domain.models.SportDomainModel

class SyncSportsDataUseCase(
    private val repository: SportsRepository
) : SuspendUseCase<Unit, Either<AppError, List<SportDomainModel>>>() {

    override suspend fun doWork(params: Unit): Either<AppError, List<SportDomainModel>> {
        return repository.syncSportsData().flatMap {
            return when (it) {
                is Either.Left<AppError> -> Either.Left<AppError>(it.value)
                is Either.Right<List<SportWithEventsPOJO>> -> Either.Right<List<SportDomainModel>>(it.value.toDomain())
                else -> {
                    // This case should not happen, but if it does, we return an unknown error
                    Either.Left<AppError>(AppError.UnknownError(Exception("Unexpected result from syncSportsData")))
                }
            }
        }
    }
}