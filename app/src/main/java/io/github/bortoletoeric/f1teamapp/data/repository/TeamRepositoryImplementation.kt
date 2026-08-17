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
    private val dao: TeamDao
) : TeamRepository {

    override fun getTeams(): Flow<List<Team>> {
        return dao.getTeams().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTeamById(teamId: String): Team {
        return dao.getTeamById(teamId).toDomain()
    }

    override suspend fun syncTeams() {
        val response = apiService.getConstructorsStandings()
        val entities = response.constructorsChampionship.map { it.toEntity() }
        dao.upsertTeamsPreservingFavorites(entities)
    }

    override suspend fun toggleFavorite(teamId: String, isFavorite: Boolean) {
        dao.updateFavoriteStatus(teamId, isFavorite)
    }

    override suspend fun getTeamDrivers(teamId: String): List<Driver> {
        val response = apiService.getTeamDrivers(teamId)
        return response.drivers.map { it.toDomain(teamId) }
    }
}