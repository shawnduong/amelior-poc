package com.hci_g1.amelior.entities

import androidx.room.*

/* Table name = "Distance" */
@Entity
data class Distance
	(
	@PrimaryKey(autoGenerate=false)
	val key: Long,			//  Epoch Time in Days
	val totalDistance: Float	//  The total calculated distance for today
)