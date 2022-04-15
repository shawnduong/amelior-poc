package com.hci_g1.amelior

import androidx.room.*
import com.hci_g1.amelior.entities.User

@Dao
interface UserDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert_user(user: User)

	@Transaction
	@Query("SELECT * FROM User WHERE key = :key LIMIT 1")
	suspend fun get_user(key: String): User
}
