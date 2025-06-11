package com.rugovit.kaizengamingcodechallange.domain.usecase

import com.rugovit.kaizengamingcodechallange.core.base.domain.FlowUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TimeTickerUseCase : FlowUseCase<Unit, Long>() {

    override fun startStream(params: Unit): Flow<Long> {
        return flow {
            while (true) {
                emit(System.currentTimeMillis())
                delay(1000) // Emit every second
            }
        }
    }
}