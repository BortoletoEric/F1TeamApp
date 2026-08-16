package io.github.bortoletoeric.f1teamapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey val id: String,
    val name: String,
    val nationality: String,
    val firstAppeareance: Int,
    val constructorsChampionships: Int,
    val driversChampionships: Int,
    val wikipediaUrl: String,
    val isFavorite: Boolean
)