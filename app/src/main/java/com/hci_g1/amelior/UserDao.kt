package com.hci_g1.amelior

import androidx.room.*
import com.hci_g1.amelior.entities.relations.*
import com.hci_g1.amelior.entities.User
import com.hci_g1.amelior.entities.Health

@Dao
interface UserDao {
    // if same, replace
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealth(health: Health)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserHealthCrossRef(crossRef: UserHealthCrossRef)

    @Transaction
    @Query("SELECT * FROM health WHERE healthId = :healthId")
    suspend fun getUsersOfHealth(healthId: String): List<HealthWithUser>

    @Transaction
    @Query("SELECT * FROM user WHERE userName = :userName")
    suspend fun getHealthOfUser(userName: String): List<UserWithHealth>

}