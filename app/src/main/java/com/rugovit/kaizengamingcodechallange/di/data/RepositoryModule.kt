package com.rugovit.kaizengamingcodechallange.di.data

import com.rugovit.kaizengamingcodechallange.core.common.TransactionRunner
import com.rugovit.kaizengamingcodechallange.data.database.AppDatabase
import com.rugovit.kaizengamingcodechallange.data.database.dao.SportDao
import com.rugovit.kaizengamingcodechallange.data.network.ApiService
import com.rugovit.kaizengamingcodechallange.data.repository.SportsRepository
import com.rugovit.kaizengamingcodechallange.data.repository.SportsRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<SportsRepository> {
        SportsRepositoryImpl(
            get<TransactionRunner>(),
            get<SportDao>(),
            get<ApiService>()
        )
    }
}