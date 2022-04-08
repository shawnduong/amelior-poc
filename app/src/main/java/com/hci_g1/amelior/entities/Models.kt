package com.hci_g1.amelior.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// table name = User
@Entity
data class User (
    @PrimaryKey(autoGenerate = false)
    val userName: String,
    val userAge: Int,
    val userSex: String,
    val userWeight: Double,
    val userHeight: Double
)

// table name = Health
@Entity
data class Health (
    @PrimaryKey(autoGenerate = false)
    val healthId: Int,
    val stepGoals: Int,
    val steps: Int,
    val gps: Double
)