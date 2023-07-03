package com.apps.heavengreen.dao

import androidx.room.*
import com.apps.heavengreen.models.TrashTransactionModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TrashTransactionDao {
    @Query("SELECT * FROM trash_transactions")
    fun getTransactions(): List<TrashTransactionModel>

    @Query("SELECT * FROM trash_transactions WHERE user_id = :userId")
    fun getTransactionsByUserId(userId: Int): TrashTransactionModel

    @Insert
    suspend fun insert(transaction: TrashTransactionModel)

    @Delete
    suspend fun delete(transaction: TrashTransactionModel)

    @Update
    suspend fun update(transaction: TrashTransactionModel)
}
