package com.rugovit.kaizengamingcodechallange.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "events",
    foreignKeys = [ForeignKey(
        entity = SportEntity::class,
        parentColumns = ["sportId"],
        childColumns = ["sportId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class EventEntity(
    @PrimaryKey val eventId: String,
    val sportId: String,
    val eventName: String,
    val startTime: Long,
    val isFavorite: Boolean = false
)