package com.hci_g1.amelior

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hci_g1.amelior.entities.relations.UserHealthCrossRef
import com.hci_g1.amelior.entities.User
import com.hci_g1.amelior.entities.Health

@Database (
    entities = [
        User::class,
        Health::class,
        UserHealthCrossRef::class
    ],
    version = 1
)
abstract class UserDatabase: RoomDatabase()
{
    abstract val userDao: UserDao
    companion object
    {
        // writes to INSTANCE, whenever we change it, it's visible
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            // makes sure that whenever we executed this block of code, it only executes by a single thread
            synchronized(this)
            {
                // when it's null, we will construct our database with room.databaseBuilder
                return INSTANCE ?: Room.databaseBuilder (
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_db"
                    // uses .also because we want to update our INSTANCE
                ) .build().also {
                    INSTANCE = it
                }
            }
        }
    }
}