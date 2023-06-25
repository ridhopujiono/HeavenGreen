package com.apps.heavengreen.repositories

import com.apps.heavengreen.dao.TrashTransactionDao
import com.apps.heavengreen.models.TrashTransactionModel
import kotlinx.coroutines.flow.Flow

class TrashTransactionRepository(private val transactionDao: TrashTransactionDao) {
    val allTrashTransactionHistory: Flow<List<TrashTransactionModel>> = transactionDao.getTransactions()

    suspend fun insertTrashTransaction(transaction: TrashTransactionDao){
        transaction.insert(transaction)
    }
    suspend fun deleteTrashTransaction(transaction: TrashTransactionDao){
        transaction.delete(transaction)
    }
    suspend fun updateTrashTransaction(transaction: TrashTransactionDao){
        transaction.update(transaction)
    }
}