package io.github.bortoletoeric.f1teamapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DriverWrapperDto(
    @SerialName("driver") val driver: DriverDto
)