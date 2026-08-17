package io.github.bortoletoeric.f1teamapp.data.mapper

import io.github.bortoletoeric.f1teamapp.data.local.entity.DriverEntity
import io.github.bortoletoeric.f1teamapp.data.remote.dto.DriverDto
import io.github.bortoletoeric.f1teamapp.domain.model.Driver

fun DriverEntity.toDomain() = Driver(
    id = id,
    teamId = teamId,
    name = name,
    photoUrl = photoUrl,
    points = points
)

fun DriverDto.toDomain(teamId: String) = Driver(
    id = driverId,
    teamId = teamId,
    name = "$name $surname",
    photoUrl = null, // URL de fotos não disponível no DTO básico de standings
    points = points.toFloat()
)

fun DriverDto.toEntity(teamId: String) = DriverEntity(
    id = driverId,
    teamId = teamId,
    name = "$name $surname",
    photoUrl = null,
    points = points.toFloat()
)