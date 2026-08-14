package io.github.bortoletoeric.f1teamapp.data.mapper

import io.github.bortoletoeric.f1teamapp.data.local.entity.DriverEntity
import io.github.bortoletoeric.f1teamapp.data.local.entity.TeamEntity
import io.github.bortoletoeric.f1teamapp.data.remote.dto.DriverDto
import io.github.bortoletoeric.f1teamapp.data.remote.dto.TeamDto
import io.github.bortoletoeric.f1teamapp.domain.model.Driver
import io.github.bortoletoeric.f1teamapp.domain.model.Team

fun TeamEntity.toDomain() = Team(
    id = id,
    name = name,
    description = description,
    logoUrl = logoUrl,
    isFavorite = isFavorite
)

fun DriverEntity.toDomain() = Driver(
    id = id,
    teamId = teamId,
    name = name,
    photoUrl = photoUrl
)

fun TeamDto.toEntity() = TeamEntity(
    id = id,
    name = name,
    description = description,
    logoUrl = logoUrl,
    isFavorite = false // Default inicial, tratado no DAO
)

fun DriverDto.toEntity(teamId: String) = DriverEntity(
    id = id,
    teamId = teamId,
    name = name,
    photoUrl = photoUrl
)