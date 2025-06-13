package com.rugovit.kaizengamingcodechallange.data.network

import com.rugovit.kaizengamingcodechallange.data.network.models.SportNetwork
import retrofit2.http.GET


interface ApiService {
    //@GET("sports_damaged.json")
     //@GET("sports_half.json")
    //@GET("sports_nodata.json")
    @GET("sports.json")
    suspend fun getSports(): List<SportNetwork>
}