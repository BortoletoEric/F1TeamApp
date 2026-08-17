package io.github.bortoletoeric.f1teamapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DriverDto(
    @SerialName("driverId") val driverId: String,
    @SerialName("name") val name: String,
    @SerialName("surname") val surname: String,
    @SerialName("nationality") val nationality: String,
    @SerialName("points") val points: Int,
    @SerialName("position") val position: Int
)