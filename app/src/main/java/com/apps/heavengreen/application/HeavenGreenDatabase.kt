package com.apps.heavengreen.application
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.apps.heavengreen.dao.TrashTransactionDao
import com.apps.heavengreen.dao.UserDao
import com.apps.heavengreen.models.TrashTransactionModel
import com.apps.heavengreen.models.UserModel

@Database(entities = [TrashTransactionModel::class, UserModel::class], version = 2, exportSchema = false)
abstract class HeavenGreenDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun trashTransactionDao(): TrashTransactionDao

    companion object {
        private var INSTANCE: HeavenGreenDatabase? = null

        fun getDatabase(
            context: Context
        ): HeavenGreenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HeavenGreenDatabase::class.java,
                    "heaven_green_database"
                )
//                    .addMigrations(migration1To2)
                    .allowMainThreadQueries()
                    .addCallback(UserSeedCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class UserSeedCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Seeding data saat tabel "users" pertama kali dibuat
        val user1 = ContentValues().apply {
            put("id", 1)
            put("username", "John Doe")
            put("password", "123")
            put("role", "buyer")
        }
        val user2 = ContentValues().apply {
            put("id", 2)
            put("username", "Jane Smith")
            put("password", "123")
            put("role", "buyer")
        }

        db.insert("users", SQLiteDatabase.CONFLICT_REPLACE, user1)
        db.insert("users", SQLiteDatabase.CONFLICT_REPLACE, user2)
    }
}