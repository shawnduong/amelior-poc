package com.hci_g1.amelior

import androidx.room.*
import com.hci_g1.amelior.entities.Mood

@Dao
interface MoodDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert_mood(mood: Mood)

	@Transaction
	@Query("SELECT * FROM Mood WHERE key = :key LIMIT 1")
	suspend fun get_mood(key: Int): Mood
}
