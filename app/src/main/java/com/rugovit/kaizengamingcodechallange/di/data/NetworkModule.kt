package com.rugovit.kaizengamingcodechallange.di.data

import com.rugovit.kaizengamingcodechallange.data.network.ApiService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    // Provide ApiService as a singleton
    single {
        Retrofit.Builder()
            //.baseUrl("https://ios-kaizen.github.io/MockSports/") // original
            .baseUrl("https://raw.githubusercontent.com/rugovit/temTest/refs/heads/main/") //testing diferent ege cases
            //TODO dont forget to change the base URL back to the original one
            .addConverterFactory(GsonConverterFactory.create()) // JSON parsing with Gson
            .build()
            .create(ApiService::class.java)
    }
}