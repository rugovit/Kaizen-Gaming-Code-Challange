package com.rugovit.kaizengamingcodechallange.core.common

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.asEither(): Flow<Either<AppError, T>> =
    this
        .map {
            Either<AppError, T>.Right(it) as Either<AppError, T>
        }
        .catch {
            emit(
                Either<AppError, T>.Left(AppError.fromException(it)) as Either<AppError, T>
            )
        }