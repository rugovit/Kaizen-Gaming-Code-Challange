package com.rugovit.kaizengamingcodechallange.ui.features.sports.viewModel

import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.ui.features.sports.models.Sport

/**
 * UI contract for Sports feature in MVI architecture.
 */
object SportsContract {

    /**
     * Represent all possible UI intents (user actions).
     */
    sealed class UiIntent {
        object Load : UiIntent()
        object SyncData : UiIntent()
        data class ToggleFavorite(val eventId: String) : UiIntent()
    }

    /**
     * UI state data. Immutable snapshot of the screen.
     */
    data class UiState(
        val isLoading: Boolean = false,
        val sports: List<Sport> = emptyList(),
        val error: AppError? = null,
        val currentTime: Long = System.currentTimeMillis()
    )

    /**
     * One-off UI effects (like showing a toast).
     */
    sealed class UiEffect {
        data class ShowError(val error: AppError) : UiEffect()
    }
}
