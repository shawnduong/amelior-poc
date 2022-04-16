package com.hci_g1.amelior.entities

import androidx.room.*

/* Table name = "User" */
@Entity
data class User
(
	@PrimaryKey(autoGenerate=false)
	val key: String,
	val name: String,
)
