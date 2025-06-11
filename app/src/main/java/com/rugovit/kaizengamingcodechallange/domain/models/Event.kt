package com.rugovit.kaizengamingcodechallange.domain.models

data class Event(
    val id: String,
    val sportId: String,
    val name: String,
    val startTime: Long,
    val isFavorite: Boolean
)
