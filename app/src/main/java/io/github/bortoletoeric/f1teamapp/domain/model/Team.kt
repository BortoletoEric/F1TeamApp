package io.github.bortoletoeric.f1teamapp.domain.model

data class Team(
    val id: String,
    val name: String,
    val nationality: String,
    val firstAppeareance: Int,
    val constructorsChampionships: Int,
    val driversChampionships: Int,
    val wikipediaUrl: String,
    val isFavorite: Boolean
)