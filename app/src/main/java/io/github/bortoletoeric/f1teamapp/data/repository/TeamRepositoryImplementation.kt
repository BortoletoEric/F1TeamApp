package io.github.bortoletoeric.f1teamapp.data.repository

import io.github.bortoletoeric.f1teamapp.data.local.dao.TeamDao
import io.github.bortoletoeric.f1teamapp.data.local.entity.TeamEntity
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
            entities.map { entity ->
                Team(
                    id = entity.id,
                    name = entity.name,
                    position = entity.position,
                    points = entity.points,
                    wins = entity.wins,
                    isFavorite = entity.isFavorite
                )
            }
        }
    }

    override suspend fun syncTeams() {
        val response = apiService.getConstructorsStandings()

        // Acessa a lista correta (constructorsChampionship) definida no DTO
        val entities = response.constructorsChampionship.map { standing ->
            TeamEntity(
                id = standing.team.teamId,
                name = standing.team.teamName,
                position = standing.position,
                points = standing.points.toFloat(), // Converte Int (DTO) para Float (Entity)
                wins = standing.wins,
                isFavorite = false
            )
        }

        dao.upsertTeamsPreservingFavorites(entities)
    }

    override suspend fun toggleFavorite(teamId: String, isFavorite: Boolean) {
        dao.updateFavoriteStatus(teamId, isFavorite)
    }

    override suspend fun getTeamDrivers(teamId: String): List<Driver> {
        val response = apiService.getTeamDrivers(teamId)
        return response.drivers.map { dto ->
            Driver(
                id = dto.driverId,
                teamId = teamId,
                name = "${dto.name} ${dto.surname}",
                photoUrl = null,
                points = dto.points
            )
        }
    }

//    data class Driver(
//        val id: String,
//        val teamId: String,
//        val name: String,
//        val photoUrl: String?,
//        val points: Int
//    )
}