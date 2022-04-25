package com.hci_g1.amelior.entities

import androidx.room.*

/* Table name = "StepCount" */
@Entity
data class StepCount
(
	@PrimaryKey(autoGenerate=false)
	val key: Long,			//  Epoch Time in Days
	val stepTotal: Float	//  The total steps for this day
)