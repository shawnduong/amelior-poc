package com.hci_g1.amelior

import androidx.room.*
import com.hci_g1.amelior.entities.Goal

@Dao
interface GoalDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert_goal(goal: Goal)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert_goal_now(goal: Goal)

	@Transaction
	@Query("SELECT * FROM Goal WHERE key = :key LIMIT 1")
	suspend fun get_goal(key: Int): Goal

	@Transaction
	@Query("SELECT * FROM Goal WHERE key = :key LIMIT 1")
	fun get_goal_now(key: Int): Goal

	@Transaction
	@Query("SELECT COUNT(*) FROM Goal")
	fun size(): Int
}
