package io.github.bortoletoeric.f1teamapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DriverResponse(
    val api: String,
    val url: String,
    val limit: Int,
    val offset: Int,
    val total: Int,
    val season: Int,
    val championshipId: String,
    val drivers: List<DriverDto>
)

@Serializable
data class DriverDto(
    val id: String,
    val teamId: String,
    val name: String,
    val photoUrl: String?
)