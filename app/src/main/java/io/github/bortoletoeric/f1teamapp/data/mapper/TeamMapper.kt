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
    isFavorite = isFavorite
)

fun ConstructorStandingDto.toEntity() = TeamEntity(
    id = teamId,
    name = team.teamName,
    position = position,
    points = points.toFloat(),
    wins = wins,
    isFavorite = false
)