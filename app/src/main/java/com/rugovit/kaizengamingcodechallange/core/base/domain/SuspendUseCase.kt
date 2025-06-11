package com.rugovit.kaizengamingcodechallange.core.base.domain

abstract class SuspendUseCase<P, R> {

    suspend operator fun invoke(params: P): R {
        return doWork(params)
    }

    abstract suspend fun doWork(params: P): R
}

