package io.github.bortoletoeric.f1teamapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConstructorsChampionshipResponseDto(
    @SerialName("constructors_championship")
    val constructorsChampionship: List<ConstructorStandingDto>
)