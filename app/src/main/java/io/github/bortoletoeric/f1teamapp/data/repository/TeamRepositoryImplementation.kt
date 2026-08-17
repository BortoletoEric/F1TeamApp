package io.github.bortoletoeric.f1teamapp.data.repository

import android.util.Log
import io.github.bortoletoeric.f1teamapp.data.local.dao.TeamDao
import io.github.bortoletoeric.f1teamapp.data.mapper.toDomain
import io.github.bortoletoeric.f1teamapp.data.mapper.toEntity
import io.github.bortoletoeric.f1teamapp.data.remote.F1ApiService
import io.github.bortoletoeric.f1teamapp.domain.model.Team
import io.github.bortoletoeric.f1teamapp.domain.model.Driver
import io.github.bortoletoeric.f1teamapp.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

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
        try {
            val response = apiService.getConstructorsStandings()
            val entities = response.constructorsChampionship.map { it.toEntity() }
            dao.upsertTeamsPreservingFavorites(entities)
        } catch (e: HttpException) {
            Log.e("TeamRepository", "Erro ao sincronizar times: Status ${e.code()} - ${e.message()}")
            throw e
        } catch (e: Exception) {
            Log.e("TeamRepository", "Erro desconhecido ao sincronizar times", e)
            throw e
        }
    }

    override suspend fun toggleFavorite(teamId: String, isFavorite: Boolean) {
        dao.updateFavoriteStatus(teamId, isFavorite)
    }

    override suspend fun getTeamDrivers(teamId: String): List<Driver> {
        return try {
            val response = apiService.getTeamDrivers(teamId)
            response.drivers.map { it.driver.toDomain(teamId) }
        } catch (e: HttpException) {
            Log.e("TeamRepository", "Erro ao buscar pilotos do time $teamId: Status ${e.code()} - ${e.message()}")
            throw e
        } catch (e: Exception) {
            Log.e("TeamRepository", "Erro desconhecido ao buscar pilotos do time $teamId", e)
            throw e
        }
    }
}