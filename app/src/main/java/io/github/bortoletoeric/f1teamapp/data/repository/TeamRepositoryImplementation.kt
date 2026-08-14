package io.github.bortoletoeric.f1teamapp.data.repository

import io.github.bortoletoeric.f1teamapp.data.local.dao.TeamDao
import io.github.bortoletoeric.f1teamapp.data.mapper.toDomain
import io.github.bortoletoeric.f1teamapp.data.mapper.toEntity
import io.github.bortoletoeric.f1teamapp.data.remote.F1ApiService
import io.github.bortoletoeric.f1teamapp.domain.model.Team
import io.github.bortoletoeric.f1teamapp.domain.model.Driver


import io.github.bortoletoeric.f1teamapp.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TeamRepositoryImpl(
    private val apiService: F1ApiService,
    private val teamDao: TeamDao
) : TeamRepository {

    // Regra 1: SSOT. Dados lidos unicamente do banco.
    override fun observeTeams(): Flow<List<Team>> {
        return teamDao.observeTeams().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeTeam(teamId: String): Flow<Team> {
        return teamDao.observeTeam(teamId).map { it.toDomain() }
    }

    override fun observeDriversByTeam(teamId: String): Flow<List<Driver>> {
        return teamDao.observeDriversByTeam(teamId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncData() {
        try {
            val remoteTeams = apiService.getCurrentTeams()

            val teamEntities = remoteTeams.map { it.toEntity() }
            val driverEntities = remoteTeams.flatMap { team ->
                team.drivers.map { driver -> driver.toEntity(teamId = team.id) }
            }

            // Atualiza times preservando estado isFavorite, depois insere os pilotos
            teamDao.upsertTeamsPreservingFavorites(teamEntities)
            teamDao.insertDrivers(driverEntities)
        } catch (e: Exception) {
            // Repassa a exceção ou loga para tratamento na UI/ViewModel
            throw e
        }
    }

    override suspend fun toggleFavorite(teamId: String, isFavorite: Boolean) {
        teamDao.updateFavoriteStatus(teamId, isFavorite)
    }
}