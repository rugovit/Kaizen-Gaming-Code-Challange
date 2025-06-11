package com.hospitalitydigital.foodorder.core.base.domain

abstract class UseCase<P, R> {

    operator fun invoke(params: P): R {
        return doWork(params)
    }

    abstract fun doWork(params: P): R
}


