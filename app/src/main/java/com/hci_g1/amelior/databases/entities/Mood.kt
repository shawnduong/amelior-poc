package com.hci_g1.amelior.entities

import androidx.room.*

/* Table name = "Mood" */
@Entity
data class Mood
(
	@PrimaryKey(autoGenerate=false)
	val key: Int,
	val timestamp: Long,
	val value: Int,
)
