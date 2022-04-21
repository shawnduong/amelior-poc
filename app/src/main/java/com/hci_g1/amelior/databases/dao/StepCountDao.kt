package com.hci_g1.amelior.databases.dao

import androidx.room.*
import com.hci_g1.amelior.entities.StepCount

@Dao
interface StepCountDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert_step_count(stepCount: StepCount)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert_step_count_now(stepCount: StepCount)

    @Transaction
    @Query("SELECT EXISTS(SELECT * FROM StepCount)")
    fun is_setup(): Boolean

    @Transaction
    @Query("SELECT * FROM StepCount WHERE key = :key LIMIT 1")
    suspend fun get_step_count(key: Int): StepCount

    @Transaction
    @Query("SELECT * FROM StepCount WHERE key = :key LIMIT 1")
    fun get_step_count_now(key: String): StepCount
}