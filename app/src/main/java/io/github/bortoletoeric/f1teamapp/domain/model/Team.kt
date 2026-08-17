package io.github.bortoletoeric.f1teamapp.domain.model

data class Team(
    val id: String,
    val name: String,
    val position: Int,
    val points: Float,
    val wins: Int,
    val season: Int,
    val isFavorite: Boolean = false
)