package com.rugovit.kaizengamingcodechallange.core.common

import androidx.sqlite.SQLiteException
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException


sealed class AppError {
    data class NetworkError(val exception: IOException) : AppError()
    data class ApiError(val exception: HttpException) : AppError()
    data class DatabaseError(val exception: SQLiteException) : AppError()
    data class UnknownError(val exception: Throwable) : AppError()

    companion object {
        fun fromException(exception: Throwable): AppError {
            Timber.e(exception, "An error occurred: ${exception.message}")
            return when (exception) {
                is IOException -> NetworkError(exception)
                is HttpException -> ApiError(exception)
                is SQLiteException -> DatabaseError(exception)
                else -> UnknownError(exception)
            }

        }
    }
}