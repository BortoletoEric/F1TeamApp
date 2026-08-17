package io.github.bortoletoeric.f1teamapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey val id: String,
    val name: String,
    val position: Int,
    val points: Float,
    val wins: Int,
    val isFavorite: Boolean = false
)