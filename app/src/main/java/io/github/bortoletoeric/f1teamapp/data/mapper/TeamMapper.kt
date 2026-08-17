package io.github.bortoletoeric.f1teamapp.data.mapper

import io.github.bortoletoeric.f1teamapp.data.local.entity.TeamEntity
import io.github.bortoletoeric.f1teamapp.data.remote.dto.ConstructorStandingDto
import io.github.bortoletoeric.f1teamapp.domain.model.Team

fun TeamEntity.toDomain() = Team(
    id = id,
    name = name,
    position = position,
    points = points,
    wins = wins,
    season = season,
    isFavorite = isFavorite
)

fun ConstructorStandingDto.toEntity(season: Int) = TeamEntity(
    id = teamId,
    name = team.teamName,
    position = position,
    points = points.toFloat(),
    wins = wins,
    season = season,
    isFavorite = false
)