package com.rugovit.kaizengamingcodechallange.domain.usecase

import com.rugovit.kaizengamingcodechallange.core.base.domain.FlowUseCase
import com.rugovit.kaizengamingcodechallange.data.repository.SportsRepository
import com.rugovit.kaizengamingcodechallange.domain.models.Sport
import com.rugovit.kaizengamingcodechallange.domain.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSportWithEventsUseCase(
    private val repository: SportsRepository
) : FlowUseCase<Unit, List<Sport>>() {

    override fun startStream(params: Unit): Flow<List<Sport>> {
        return repository.getSportsWithEvents()
            .map { sportWithEventsList ->
                sportWithEventsList.toDomain()
            }
    }
}