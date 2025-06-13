package com.rugovit.kaizengamingcodechallange.core.common

interface TransactionRunner {
    suspend fun <T> run(block: suspend () -> T): T
}