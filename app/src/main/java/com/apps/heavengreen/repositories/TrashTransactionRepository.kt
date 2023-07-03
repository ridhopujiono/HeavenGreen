package com.apps.heavengreen.repositories

import com.apps.heavengreen.dao.TrashTransactionDao
import com.apps.heavengreen.models.TrashTransactionModel
import kotlinx.coroutines.flow.Flow

class TrashTransactionRepository(private val transactionDao: TrashTransactionDao) {

    suspend fun getAllTrashTransaction(): List<TrashTransactionModel> {
        return transactionDao.getTransactions()
    }

    suspend fun getTrashTransactionByUserId(userId: Int): TrashTransactionModel {
        return transactionDao.getTransactionsByUserId(userId)
    }

    suspend fun insertTrashTransaction(transaction: TrashTransactionModel){
        transactionDao.insert(transaction)
    }
    suspend fun deleteTrashTransaction(transaction: TrashTransactionModel){
        transactionDao.delete(transaction)
    }
    suspend fun updateTrashTransaction(transaction: TrashTransactionModel){
        transactionDao.update(transaction)
    }
}