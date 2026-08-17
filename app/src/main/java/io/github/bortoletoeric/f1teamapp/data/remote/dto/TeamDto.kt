package io.github.bortoletoeric.f1teamapp.data.remote.dto

// TeamDto.kt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamDto(
    @SerialName("teamId") val teamId: String,
    @SerialName("teamName") val teamName: String,
    @SerialName("base") val base: String
)