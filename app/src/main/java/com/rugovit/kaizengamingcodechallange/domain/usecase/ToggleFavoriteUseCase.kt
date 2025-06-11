package com.rugovit.kaizengamingcodechallange.domain.usecase

import arrow.core.Either
import com.rugovit.kaizengamingcodechallange.core.base.domain.SuspendUseCase
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.data.repository.SportsRepository

class ToggleFavoriteUseCase(
    private val repository: SportsRepository
) : SuspendUseCase<String, Either<AppError, Unit>>() {

    override suspend fun doWork(params: String): Either<AppError, Unit> {
        return repository.toggleFavorite(params)
    }
}