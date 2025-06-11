package com.rugovit.kaizengamingcodechallange.di.view

import com.rugovit.kaizengamingcodechallange.ui.features.sports.SportsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SportsViewModel(get(), get(),get(), get())
    }
}