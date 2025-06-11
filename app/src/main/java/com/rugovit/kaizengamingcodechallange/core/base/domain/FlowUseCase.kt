package com.rugovit.kaizengamingcodechallange.core.base.domain
import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<P, R> {

     operator fun invoke(params: P): Flow<R> {
        return startStream(params)
    }

    abstract  fun startStream(params: P): Flow<R>
}

