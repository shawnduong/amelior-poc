package com.hci_g1.amelior

import androidx.room.*
import com.hci_g1.amelior.entities.Mood

@Dao
interface MoodDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert_mood(mood: Mood)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert_mood_now(mood: Mood)

	@Transaction
	@Query("SELECT * FROM Mood WHERE key = :key LIMIT 1")
	suspend fun get_mood(key: Int): Mood

	@Transaction
	@Query("SELECT * FROM Mood WHERE key = :key LIMIT 1")
	fun get_mood_now(key: Int): Mood

	@Transaction
	@Query("SELECT COUNT(*) FROM Mood")
	fun size(): Int
}
