package com.rugovit.kaizengamingcodechallange.core.common

import androidx.sqlite.SQLiteException
import retrofit2.HttpException
import java.io.IOException


sealed class AppError : Exception() {
    data class NetworkError(val exception: IOException) : AppError()
    data class ApiError(val exception: HttpException) : AppError()
    data class DatabaseError(val exception: SQLiteException) : AppError()
    data class UnknownError(val exception: Exception) : AppError()

    companion object {
        fun fromException(exception: Exception): AppError {
            return when (exception) {
                is IOException -> NetworkError(exception)
                is HttpException -> ApiError(exception)
                is SQLiteException -> DatabaseError(exception)
                else -> UnknownError(exception)
            }
        }
    }
}