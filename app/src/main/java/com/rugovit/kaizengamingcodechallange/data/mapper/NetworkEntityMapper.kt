package com.rugovit.kaizengamingcodechallange.data.mapper

import com.rugovit.kaizengamingcodechallange.data.database.entities.EventEntity
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportEntity
import com.rugovit.kaizengamingcodechallange.data.network.models.EventNetwork
import com.rugovit.kaizengamingcodechallange.data.network.models.SportNetwork

fun SportNetwork.toEntity() = SportEntity(
    sportId = i,
    sportName = d
)

fun EventNetwork.toEntity(isFavorite: Boolean) = EventEntity(
    eventId = i,
    sportId = si,
    eventName = d,
    startTime = tt,
    isFavorite = isFavorite
)