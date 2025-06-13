package com.rugovit.kaizengamingcodechallange.ui.features.sports.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.getOrElse
import com.rugovit.kaizengamingcodechallange.core.common.asEither
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.domain.models.SportDomainModel
import com.rugovit.kaizengamingcodechallange.domain.usecase.GetSportWithEventsUseCase
import com.rugovit.kaizengamingcodechallange.domain.usecase.SyncSportsDataUseCase
import com.rugovit.kaizengamingcodechallange.domain.usecase.TimeTickerUseCase
import com.rugovit.kaizengamingcodechallange.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.rugovit.kaizengamingcodechallange.core.base.domain.invoke
import com.rugovit.kaizengamingcodechallange.ui.features.sports.mappers.toSport


class SportsViewModel(
    private val getSportWithEventsUseCase: GetSportWithEventsUseCase,
    private val syncSportsDataUseCase: SyncSportsDataUseCase,
    private val timeTickerUseCase: TimeTickerUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val intentFlow = MutableSharedFlow<SportsContract.UiIntent>()
    private val _state = MutableStateFlow(SportsContract.UiState())
    val uiState: StateFlow<SportsContract.UiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SportsContract.UiEffect>()
    val effect: SharedFlow<SportsContract.UiEffect> = _effect.asSharedFlow()

    init {
        collectIntents()

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true)}
            getSportWithEventsUseCase()
                .asEither()
                .collect { result: Either<AppError, List<SportDomainModel>> ->
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            sports = result.map { domainList ->
                                domainList.map { it.toSport() }
                            }.getOrElse { emptyList() },
                            error = result.fold({ it }, { null })
                        )
                    }

                    result.fold(
                        { error -> _effect.emit(SportsContract.UiEffect.ShowError(error)) },
                        {}
                    )
                }
        }

        viewModelScope.launch {
            timeTickerUseCase()
                .collect { now ->
                    _state.update { state ->
                        state.copy(
                            currentTime = System.currentTimeMillis() / 1000
                        )}
                }
        }

        // Trigger initial load and sync
        process(SportsContract.UiIntent.Load)
        process(SportsContract.UiIntent.SyncData)
    }

    /**
     * Called by the view to push user intents.
     */
    fun process(intent: SportsContract.UiIntent) {
        viewModelScope.launch {
            intentFlow.emit(intent)
        }
    }

    private fun collectIntents() {
        viewModelScope.launch {
            intentFlow.collect { intent ->
                when (intent) {
                    is SportsContract.UiIntent.Load -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is SportsContract.UiIntent.SyncData -> {
                        syncData()
                    }
                    is SportsContract.UiIntent.ToggleFavorite -> {
                        toggleFavorite(intent.eventId)
                    }
                }
            }
        }
    }

    private fun syncData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true)}
            syncSportsDataUseCase().fold(
                { error -> _effect.emit(SportsContract.UiEffect.ShowError(error)) },
                {   _state.update { it.copy(isLoading = false)}}
            )
        }
    }

    private fun toggleFavorite(eventId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(eventId).fold(
                { error -> _effect.emit(SportsContract.UiEffect.ShowError(error)) },
                { /* UI updates via data flow */ }
            )
        }
    }
}
