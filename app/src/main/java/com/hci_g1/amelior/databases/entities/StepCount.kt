package com.hci_g1.amelior.entities

import androidx.room.*

/* Table name = "StepCount" */
@Entity
data class StepCount
(
    @PrimaryKey(autoGenerate=false)
    val key: Int,
    val timestamp: Long,
    val steps: Int
)