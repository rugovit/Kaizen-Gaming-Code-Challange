package com.rugovit.kaizengamingcodechallange.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class SportWithEventsEntity(
    @Embedded val sport: SportEntity,
    @Relation(
        parentColumn = "sportId",
        entityColumn = "sportId"
    )
    val events: List<EventEntity>
)