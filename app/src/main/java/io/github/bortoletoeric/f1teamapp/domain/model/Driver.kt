package io.github.bortoletoeric.f1teamapp.domain.model

data class Driver(
    val id: String,
    val teamId: String,
    val name: String,
    val photoUrl: String?
)