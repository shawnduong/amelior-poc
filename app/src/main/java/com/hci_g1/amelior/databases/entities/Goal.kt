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
	var localProgress: Long,
	var lastCompleted: Long,

	/* This is terrible, but we have an MVP to meet. This is very untested over
	   longer time scales. Never write the code I've written below. -Shawn */
	var hist0: Long,
	var hist1: Long,
	var hist2: Long,
	var hist3: Long,
	var hist4: Long,
	var hist5: Long,
	var hist6: Long,
)
