package com.apps.heavengreen.dao

import androidx.room.*
import com.apps.heavengreen.models.TrashTransactionModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TrashTransactionDao {
    @Query("SELECT * FROM trash_transactions")
    fun getTransactions(): Flow<List<TrashTransactionModel>>

    @Insert
    suspend fun insert(transaction: TrashTransactionDao)

    @Delete
    suspend fun delete(transaction: TrashTransactionDao)

    @Update
    suspend fun update(transaction: TrashTransactionDao)
}
