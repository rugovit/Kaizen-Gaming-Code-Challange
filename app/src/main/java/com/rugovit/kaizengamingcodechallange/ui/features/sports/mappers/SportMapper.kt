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

    val competitors = name.split("-")
    val competitor1 = competitors.getOrNull(0)?.trim() ?: ""
    val competitor2 = competitors.getOrNull(1)?.trim() ?: ""
    return Event(
        id = id,
        sportId = sportId,
        startTime = startTime,
        competitor1 = competitor1,
        competitor2 = competitor2,
        isFavorite = isFavorite
    )
}
