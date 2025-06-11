package com.rugovit.kaizengamingcodechallange.di.data

import com.rugovit.kaizengamingcodechallange.data.database.AppDatabase
import com.rugovit.kaizengamingcodechallange.data.database.dao.SportDao
import com.rugovit.kaizengamingcodechallange.data.network.ApiService
import com.rugovit.kaizengamingcodechallange.data.repository.SportsRepository
import com.rugovit.kaizengamingcodechallange.data.repository.SportsRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    // Provide SportsRepository as a singleton, implemented by SportsRepositoryImpl
    single<SportsRepository> {
        SportsRepositoryImpl(
            get<AppDatabase>(), // Inject AppDatabase
            get<SportDao>(),    // Inject SportDao
            get<ApiService>()   // Inject ApiService
        )
    }
}