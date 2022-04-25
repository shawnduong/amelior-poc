package com.hci_g1.amelior

import androidx.room.*
import com.hci_g1.amelior.entities.Distance

@Dao
interface DistanceDao
{
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert_distance(distance: Distance)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert_distance_now(distance: Distance)
	
	@Transaction
	@Query("SELECT EXISTS(SELECT * FROM Distance WHERE key = :key LIMIT 1)")
	fun distance_exists_now(key: Long): Boolean
	
	@Transaction
	@Query("SELECT * FROM Distance WHERE key = :key LIMIT 1")
	suspend fun get_distance(key: Long): Distance
	
	@Transaction
	@Query("SELECT * FROM Distance WHERE key = :key LIMIT 1")
	fun get_distance_now(key: Long): Distance
}