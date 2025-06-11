package com.rugovit.kaizengamingcodechallange.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.core.common.asEither
import com.rugovit.kaizengamingcodechallange.domain.models.Sport
import com.rugovit.kaizengamingcodechallange.domain.usecase.GetSportWithEventsUseCase
import com.rugovit.kaizengamingcodechallange.domain.usecase.SyncSportsDataUseCase
import com.rugovit.kaizengamingcodechallange.domain.usecase.TimeTickerUseCase
import com.rugovit.kaizengamingcodechallange.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.rugovit.kaizengamingcodechallange.core.base.domain.invoke


class SportsViewModel(private val getSportWithEventsUseCase: GetSportWithEventsUseCase,
                      private val syncSportsDataUseCase: SyncSportsDataUseCase,
                      private val timeTickerUseCase: TimeTickerUseCase,
                      private val toggleFavoriteUseCase: ToggleFavoriteUseCase) : ViewModel() {


    // UI state for sports data
    val sportsState: StateFlow<Either<AppError, List<Sport>>> = getSportWithEventsUseCase()
        .asEither()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Either.Right(emptyList()) // Initial empty state
        )

    // UI state for real-time updates
    val currentTime: StateFlow<Long> = timeTickerUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = System.currentTimeMillis()
        )

    init {
        syncData() // Sync data when the ViewModel is created
    }

    fun syncData() {
        viewModelScope.launch {
            syncSportsDataUseCase().fold(
                { error -> /* Optionally handle error, e.g., log it */ },
                { /* Sync successful, data will flow through sportsState */ }
            )
        }
    }

    fun toggleFavorite(eventId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(eventId).fold(
                { error -> /* Optionally handle error */ },
                { /* Toggle successful, UI updates via sportsState */ }
            )
        }
    }
}