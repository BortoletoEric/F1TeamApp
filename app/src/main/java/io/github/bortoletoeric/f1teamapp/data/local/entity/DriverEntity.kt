package io.github.bortoletoeric.f1teamapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "drivers",
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["teamId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DriverEntity(
    @PrimaryKey val id: String,
    val teamId: String,
    val name: String,
    val surname: String,
    val number: Int?,
    val nationality: String,
    val birthday: String,
    val position: Int,
    val photoUrl: String?,
    val points: Float
)