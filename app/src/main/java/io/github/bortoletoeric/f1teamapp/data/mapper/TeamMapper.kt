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
    nationality = nationality,
    firstAppeareance = firstAppeareance,
    constructorsChampionships = constructorsChampionships,
    driversChampionships = driversChampionships,
    wikipediaUrl = wikipediaUrl,
    isFavorite = isFavorite
)

fun TeamDto.toEntity() = TeamEntity(
    id = teamId,
    name = teamName,
    nationality = teamNationality,
    firstAppeareance = firstAppeareance,
    constructorsChampionships = constructorsChampionships ?: 0,
    driversChampionships = driversChampionships ?: 0,
    wikipediaUrl = url,
    isFavorite = false
)

fun DriverEntity.toDomain() = Driver(
    id = id,
    teamId = teamId,
    name = name,
    photoUrl = photoUrl
)

fun DriverDto.toEntity(teamId: String) = DriverEntity(
    id = id,
    teamId = teamId,
    name = name,
    photoUrl = photoUrl
)