package com.rugovit.kaizengamingcodechallange.domain.models

data class Sport(
    val id: String,
    val name: String,
    val events: List<Event>
)
