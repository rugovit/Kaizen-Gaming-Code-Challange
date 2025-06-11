package com.rugovit.kaizengamingcodechallange.core.common

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.asEither(): Flow<Either<AppError, T>> =
    this
        .map { Either.Right(it) }
        .catch { emit(Either.Left(AppError.fromException(it as Exception))) }