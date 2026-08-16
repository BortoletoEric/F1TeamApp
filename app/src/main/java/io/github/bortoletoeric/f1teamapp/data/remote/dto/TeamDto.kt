package io.github.bortoletoeric.f1teamapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TeamResponse(
    val api: String,
    val url: String,
    val limit: Int,
    val offset: Int,
    val total: Int,
    val season: Int,
    val championshipId: String,
    val teams: List<TeamDto>
)

@Serializable
data class TeamDto(
    val teamId: String,
    val teamName: String,
    val teamNationality: String,
    val firstAppeareance: Int,
    val constructorsChampionships: Int?, // Obrigatório ser anulável
    val driversChampionships: Int?,      // Obrigatório ser anulável
    val url: String
)