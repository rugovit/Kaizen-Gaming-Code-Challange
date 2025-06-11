package com.rugovit.kaizengamingcodechallange.di.domain

import com.rugovit.kaizengamingcodechallange.domain.usecase.GetSportWithEventsUseCase
import com.rugovit.kaizengamingcodechallange.domain.usecase.SyncSportsDataUseCase
import com.rugovit.kaizengamingcodechallange.domain.usecase.TimeTickerUseCase
import com.rugovit.kaizengamingcodechallange.domain.usecase.ToggleFavoriteUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetSportWithEventsUseCase(get()) }
    single { SyncSportsDataUseCase(get()) }
    single { TimeTickerUseCase() }
    single { ToggleFavoriteUseCase(get()) }
}