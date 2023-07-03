package com.apps.heavengreen.dao

import androidx.room.*
import com.apps.heavengreen.models.UserModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UserModel>>

    @Insert
    suspend fun insert(user: UserModel)

    @Delete
    suspend fun delete(user: UserModel)

    @Update
    suspend fun update(user: UserModel)
}
