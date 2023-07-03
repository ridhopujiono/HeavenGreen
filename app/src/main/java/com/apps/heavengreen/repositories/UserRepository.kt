package com.apps.heavengreen.repositories

import com.apps.heavengreen.dao.TrashTransactionDao
import com.apps.heavengreen.dao.UserDao
import com.apps.heavengreen.models.TrashTransactionModel
import com.apps.heavengreen.models.UserModel
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allUser: Flow<List<UserModel>> = userDao.getUsers()

    suspend fun insertUser(user: UserModel){
        userDao.insert(user)
    }
    suspend fun deleteUser(user: UserModel){
        userDao.delete(user)
    }
    suspend fun updateUser(user: UserModel){
        userDao.update(user)
    }
}