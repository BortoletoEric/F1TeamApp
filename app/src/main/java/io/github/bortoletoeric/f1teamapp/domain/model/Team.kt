package io.github.bortoletoeric.f1teamapp.domain.model

data class Team(
    val id: String,
    val name: String,
    val description: String,
    val logoUrl: String?,
    val isFavorite: Boolean
)