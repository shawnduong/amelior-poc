package com.hci_g1.amelior.entities

import androidx.room.*

/* Table name = "Goal" */
@Entity
data class Goal
(
	@PrimaryKey(autoGenerate=false)
	val key: Int,
	val custom: Boolean,
	val action: String,
	val quantity: Int,
	val units: String,
	val frequency: String,
	var level: Int,
	var lastCompleted: Long,
)
