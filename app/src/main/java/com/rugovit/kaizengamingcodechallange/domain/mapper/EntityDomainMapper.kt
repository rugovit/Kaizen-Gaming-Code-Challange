package com.rugovit.kaizengamingcodechallange.domain.mapper

import com.rugovit.kaizengamingcodechallange.data.database.entities.EventEntity
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportWithEventsPOJO
import com.rugovit.kaizengamingcodechallange.domain.models.EventDomainModel
import com.rugovit.kaizengamingcodechallange.domain.models.SportDomainModel

fun SportWithEventsPOJO.toDomain(): SportDomainModel{
    return SportDomainModel(
        id = sport.sportId,
        name = sport.sportName,
        eventDomainModels = events.map { it.toDomain() }
    )
}
fun List<SportWithEventsPOJO>.toDomain(): List<SportDomainModel> {
    return map { it.toDomain() }
}

fun EventEntity.toDomain(): EventDomainModel {
    return EventDomainModel(
        id = eventId,
        sportId = sportId,
        name = eventName,
        startTime = startTime,
        isFavorite = isFavorite
    )
}