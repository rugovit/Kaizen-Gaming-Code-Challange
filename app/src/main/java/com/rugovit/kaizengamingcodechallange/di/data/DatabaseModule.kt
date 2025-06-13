package com.rugovit.kaizengamingcodechallange.di.data

import androidx.room.Room
import com.rugovit.kaizengamingcodechallange.core.common.TransactionRunner
import com.rugovit.kaizengamingcodechallange.data.database.AppDatabase
import com.rugovit.kaizengamingcodechallange.data.database.RoomTransactionRunner
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    // Provide AppDatabase as a singleton
    single {
        Room.databaseBuilder(
            androidApplication(), // Application context provided by Koin
            AppDatabase::class.java,
            "sports_database" // Database name
        ).build()
    }
    // Provide SportDao as a singleton, retrieved from AppDatabase
    single { get<AppDatabase>().sportDao() }
    // add transaction runner
    single<TransactionRunner>{
         RoomTransactionRunner(get<AppDatabase>())
    }
}