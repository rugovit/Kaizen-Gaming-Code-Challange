package com.rugovit.kaizengamingcodechallange.ui.features.sports.mappers

import com.rugovit.kaizengamingcodechallange.domain.models.EventDomainModel
import com.rugovit.kaizengamingcodechallange.domain.models.SportDomainModel
import com.rugovit.kaizengamingcodechallange.ui.features.sports.models.Event
import com.rugovit.kaizengamingcodechallange.ui.features.sports.models.Sport


fun SportDomainModel.toSport(): Sport{
    return Sport(
        id = id,
        name = name,
        events = eventDomainModels.map { it.toEvent() }
    )
}

fun EventDomainModel.toEvent(): Event {
    return Event(
        id = id,
        sportId = sportId,
        name = name,
        timeRemaining = formatTimeRemaining(startTime),
        isFavorite = isFavorite
    )
}

fun formatTimeRemaining(seconds: Long): String {
    val days = seconds / 86400
    val hours = (seconds % 86400) / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return buildString {
        if (days > 0) append("${days}d ")
        if (hours > 0 || days > 0) append("${hours}h ")
        if (minutes > 0 || hours > 0 || days > 0) append("${minutes}m ")
        append("${secs}s")
    }.trim()
}