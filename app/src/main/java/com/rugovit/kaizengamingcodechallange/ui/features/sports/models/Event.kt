package com.rugovit.kaizengamingcodechallange.ui.features.sports.models

class Event(
    val id: String,
    val sportId: String,
    val startTime: Long,
    val competitor1 : String,
    val competitor2 : String,
    val isFavorite: Boolean
)