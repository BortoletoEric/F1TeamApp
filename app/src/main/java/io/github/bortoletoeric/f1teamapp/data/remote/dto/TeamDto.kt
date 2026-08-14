package io.github.bortoletoeric.f1teamapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TeamDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("logoUrl") val logoUrl: String?,
    @SerializedName("drivers") val drivers: List<DriverDto>
)

data class DriverDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("photoUrl") val photoUrl: String?
)