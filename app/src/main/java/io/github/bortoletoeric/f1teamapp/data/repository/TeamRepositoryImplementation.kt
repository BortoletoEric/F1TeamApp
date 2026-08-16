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
            val teamResponse = apiService.getTeams()
            val remoteTeams = teamResponse.teams

            val teamEntities = remoteTeams.map { it.toEntity() }
            teamDao.upsertTeamsPreservingFavorites(teamEntities)

            val driverEntities = remoteTeams.flatMap { team ->
                val driversResponse = apiService.getDriversByTeam(team.teamId)
                driversResponse.drivers.map { driver ->
                    driver.toEntity(teamId = team.teamId)
                }
            }

            teamDao.insertDrivers(driverEntities)

        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun toggleFavorite(teamId: String, isFavorite: Boolean) {
        teamDao.updateFavoriteStatus(teamId, isFavorite)
    }
}