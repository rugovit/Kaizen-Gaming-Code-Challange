package com.rugovit.kaizengamingcodechallange.core.base.domain

import com.hospitalitydigital.foodorder.core.base.domain.UseCase
import kotlinx.coroutines.flow.Flow

operator fun <R> FlowUseCase<Unit, R>.invoke(): Flow<R> = invoke(Unit)
suspend operator fun <R> SuspendUseCase<Unit, R>.invoke() = invoke(Unit)
operator fun <R> UseCase<Unit, R>.invoke() = invoke(Unit)