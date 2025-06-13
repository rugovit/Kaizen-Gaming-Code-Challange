package com.rugovit.kaizengamingcodechallange.data.database

import androidx.room.withTransaction
import com.rugovit.kaizengamingcodechallange.core.common.TransactionRunner

class RoomTransactionRunner(private val db: AppDatabase) : TransactionRunner {
    override suspend fun <T> run(block: suspend () -> T): T =
        db.withTransaction { block() }
}