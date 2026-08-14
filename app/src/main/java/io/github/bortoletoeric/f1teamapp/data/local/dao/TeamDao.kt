package io.github.bortoletoeric.f1teamapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.github.bortoletoeric.f1teamapp.data.local.entity.DriverEntity
import io.github.bortoletoeric.f1teamapp.data.local.entity.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    // Regra 2: Ordenação por Descrição obrigatória diretamente na query
    @Query("SELECT * FROM teams ORDER BY description ASC")
    fun observeTeams(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE id = :teamId")
    fun observeTeam(teamId: String): Flow<TeamEntity>

    @Query("SELECT * FROM drivers WHERE teamId = :teamId")
    fun observeDriversByTeam(teamId: String): Flow<List<DriverEntity>>

    @Query("SELECT isFavorite FROM teams WHERE id = :teamId")
    suspend fun getFavoriteStatus(teamId: String): Boolean?

    @Query("UPDATE teams SET isFavorite = :isFavorite WHERE id = :teamId")
    suspend fun updateFavoriteStatus(teamId: String, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: TeamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrivers(drivers: List<DriverEntity>)

    // Regra 3: Preservação do status de favorito local durante o Upsert
    @Transaction
    suspend fun upsertTeamsPreservingFavorites(teams: List<TeamEntity>) {
        teams.forEach { remoteTeam ->
            val isFavorite = getFavoriteStatus(remoteTeam.id) ?: false
            insertTeam(remoteTeam.copy(isFavorite = isFavorite))
        }
    }
}