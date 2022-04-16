package com.hci_g1.amelior

import androidx.room.*
import com.hci_g1.amelior.entities.User

@Dao
interface UserDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert_user(user: User)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert_user_now(user: User)

	@Transaction
	@Query("SELECT EXISTS(SELECT * FROM User)")
	fun is_setup(): Boolean

	@Transaction
	@Query("SELECT * FROM User WHERE key = :key LIMIT 1")
	suspend fun get_user(key: String): User

	@Transaction
	@Query("SELECT * FROM User WHERE key = :key LIMIT 1")
	fun get_user_now(key: String): User
}
