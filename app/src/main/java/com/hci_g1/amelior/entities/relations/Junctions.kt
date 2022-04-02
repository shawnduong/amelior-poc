package com.hci_g1.amelior.entities.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.hci_g1.amelior.entities.Health
import com.hci_g1.amelior.entities.User

@Entity(primaryKeys = ["userName","healthId"])
data class UserHealthCrossRef (
    val userName: String,
    val healthId: Int
)

data class UserWithHealth (
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "userName",
        // foreign
        entityColumn = "healthId",
        associateBy = Junction (UserHealthCrossRef::class)
    )
    val subjects: List<Health>
)

data class HealthWithUser (
    @Embedded
    val health: Health,
    @Relation(
        parentColumn = "healthId",
        // foreign
        entityColumn = "userName",
        associateBy = Junction (UserHealthCrossRef::class)
    )
    val users: List<User>
)