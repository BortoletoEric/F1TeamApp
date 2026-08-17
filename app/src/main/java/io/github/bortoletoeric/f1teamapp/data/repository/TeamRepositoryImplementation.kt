package io.github.bortoletoeric.f1teamapp.data.repository

import android.util.Log
import io.github.bortoletoeric.f1teamapp.data.local.dao.TeamDao
import io.github.bortoletoeric.f1teamapp.data.local.dao.DriverDao
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
    private val teamDao: TeamDao,
    private val driverDao: DriverDao
) : TeamRepository {

    override fun getTeams(): Flow<List<Team>> {
        return teamDao.getTeams().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTeamById(teamId: String): Team {
        return teamDao.getTeamById(teamId).toDomain()
    }

    override suspend fun syncTeams() {
        try {
            val response = apiService.getConstructorsStandings()
            val season = response.season
            val entities = response.constructorsChampionship.map { it.toEntity(season) }
            teamDao.upsertTeamsPreservingFavorites(entities)
        } catch (e: HttpException) {
            Log.e("TeamRepository", "Erro ao sincronizar times: Status ${e.code()} - ${e.message()}")
            throw e
        } catch (e: Exception) {
            Log.e("TeamRepository", "Erro desconhecido ao sincronizar times", e)
            throw e
        }
    }

    override suspend fun toggleFavorite(teamId: String, isFavorite: Boolean) {
        teamDao.updateFavoriteStatus(teamId, isFavorite)
    }

    override suspend fun getTeamDrivers(teamId: String): List<Driver> {
        // 1. Tenta buscar do cache local
        val cachedDrivers = driverDao.getDriversByTeamId(teamId)
        if (cachedDrivers.isNotEmpty()) {
            Log.d("TeamRepository", "Retornando pilotos do cache para o time: $teamId")
            return cachedDrivers.map { it.toDomain() }
        }

        // 2. Se não houver cache, busca da API
        return try {
            Log.d("TeamRepository", "Buscando pilotos da API para o time: $teamId")
            val response = apiService.getTeamDrivers(teamId)
            val drivers = response.drivers.map { it.driver.toDomain(teamId) }

            // 3. Salva no cache para uso futuro
            driverDao.insertDrivers(drivers.map { it.toEntity() })

            drivers
        } catch (e: HttpException) {
            Log.e("TeamRepository", "Erro ao buscar pilotos do time $teamId: Status ${e.code()} - ${e.message()}")
            throw e
        } catch (e: Exception) {
            Log.e("TeamRepository", "Erro desconhecido ao buscar pilotos do time $teamId", e)
            throw e
        }
    }
}