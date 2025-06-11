package com.rugovit.kaizengamingcodechallange.data.network

import com.rugovit.kaizengamingcodechallange.data.network.models.SportNetwork
import retrofit2.http.GET


interface ApiService {
    @GET("sports.json")
    suspend fun getSports(): List<SportNetwork>
}