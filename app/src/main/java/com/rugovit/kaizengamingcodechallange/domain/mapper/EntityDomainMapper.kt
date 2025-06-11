package com.rugovit.kaizengamingcodechallange.domain.mapper

import com.rugovit.kaizengamingcodechallange.data.database.entities.EventEntity
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportWithEventsEntity
import com.rugovit.kaizengamingcodechallange.domain.models.Event
import com.rugovit.kaizengamingcodechallange.domain.models.Sport

fun SportWithEventsEntity.toDomain(): Sport{
    return Sport(
        id = sport.sportId,
        name = sport.sportName,
        events = events.map { it.toDomain() }
    )
}
fun List<SportWithEventsEntity>.toDomain(): List<Sport> {
    return map { it.toDomain() }
}

fun EventEntity.toDomain(): Event {
    return Event(
        id = eventId,
        sportId = sportId,
        name = eventName,
        startTime = startTime,
        isFavorite = isFavorite
    )
}