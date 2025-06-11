package com.rugovit.kaizengamingcodechallange

import android.app.Application
import com.rugovit.kaizengamingcodechallange.di.data.databaseModule
import com.rugovit.kaizengamingcodechallange.di.data.networkModule
import com.rugovit.kaizengamingcodechallange.di.data.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class SportApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SportApplication)
            modules(listOf(databaseModule, networkModule, repositoryModule))
        }
    }
}