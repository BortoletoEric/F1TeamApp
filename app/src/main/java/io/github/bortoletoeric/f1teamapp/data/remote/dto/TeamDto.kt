package io.github.bortoletoeric.f1teamapp.data.remote.dto

// TeamDto.kt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamDto(
    @SerialName("teamName") val teamName: String,
    @SerialName("country") val country: String
)