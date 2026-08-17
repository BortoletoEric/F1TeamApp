package io.github.bortoletoeric.f1teamapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConstructorStandingDto(
    @SerialName("position") val position: Int,
    @SerialName("points") val points: Int,
    @SerialName("wins") val wins: Int,
    @SerialName("team") val team: TeamDto
)