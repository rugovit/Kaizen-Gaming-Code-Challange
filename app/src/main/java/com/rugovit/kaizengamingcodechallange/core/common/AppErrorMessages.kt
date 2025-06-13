package com.rugovit.kaizengamingcodechallange.core.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rugovit.kaizengamingcodechallange.R

@Composable
fun AppError.toUserMessage(): String = when (this) {
    is AppError.NetworkError  -> stringResource(R.string.error_no_internet)
    is AppError.ApiError      -> stringResource(R.string.error_server)
    is AppError.DatabaseError -> stringResource(R.string.error_database)
    is AppError.UnknownError  -> stringResource(R.string.error_unknown)
}