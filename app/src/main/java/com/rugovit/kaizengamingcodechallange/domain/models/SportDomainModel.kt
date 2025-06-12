package com.rugovit.kaizengamingcodechallange.domain.models

data class SportDomainModel(
    val id: String,
    val name: String,
    val eventDomainModels: List<EventDomainModel>
)
