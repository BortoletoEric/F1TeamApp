package io.github.bortoletoeric.f1teamapp.domain.repository

import io.github.bortoletoeric.f1teamapp.domain.model.Driver
import io.github.bortoletoeric.f1teamapp.domain.model.Team
import kotlinx.coroutines.flow.Flow

interface TeamRepository {
    // SSOT: Consome dados exclusivamente do Room (já ordenados)
    fun getTeams(): Flow<List<Team>>

    // Busca na API os times do campeonato (/current/constructors-championship)
    suspend fun getTeamById(teamId: String): Team

    // Atualiza o banco com dados da API (/current/constructors-championship) mantendo favoritos
    suspend fun syncTeams()

    // Operação estritamente local
    suspend fun toggleFavorite(teamId: String, isFavorite: Boolean)

    // Busca na API os pilotos de uma equipe (/teams/{teamId}/drivers)
    suspend fun getTeamDrivers(teamId: String): List<Driver>
}