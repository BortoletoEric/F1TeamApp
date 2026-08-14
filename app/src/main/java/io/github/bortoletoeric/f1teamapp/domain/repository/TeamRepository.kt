package io.github.bortoletoeric.f1teamapp.domain.repository

import io.github.bortoletoeric.f1teamapp.domain.model.Driver
import io.github.bortoletoeric.f1teamapp.domain.model.Team
import kotlinx.coroutines.flow.Flow

interface TeamRepository {
    /**
     * SSOT: Observa a lista de times diretamente do banco local.
     * Regra de negócio: A ordenação por "Descrição" deverá ser aplicada na query do DAO.
     */
    fun observeTeams(): Flow<List<Team>>

    /**
     * Observa um time específico para a tela de detalhes.
     */
    fun observeTeam(teamId: String): Flow<Team>

    /**
     * Observa a lista de pilotos associada a um time específico.
     */
    fun observeDriversByTeam(teamId: String): Flow<List<Driver>>

    /**
     * Faz o fetch na API (f1api.dev) e faz o upsert no banco de dados local.
     * O contrato exige que a implementação preserve o status `isFavorite` já existente no Room.
     */
    suspend fun syncData()

    /**
     * Atualiza o estado de favorito de um time.
     * Essa alteração deve ocorrer estritamente no banco de dados local (Room).
     */
    suspend fun toggleFavorite(teamId: String, isFavorite: Boolean)
}