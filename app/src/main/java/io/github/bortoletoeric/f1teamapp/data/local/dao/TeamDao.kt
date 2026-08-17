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
    // Garante a ordenação nativa (posição) conforme regra de negócio
    @Query("SELECT * FROM teams ORDER BY position ASC")
    fun getTeams(): Flow<List<TeamEntity>>

    @Query("SELECT isFavorite FROM teams WHERE id = :teamId")
    suspend fun isFavorite(teamId: String): Boolean?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: TeamEntity)

    // Lógica de Upsert que preserva o status de favorito local
    @Transaction
    suspend fun upsertTeamsPreservingFavorites(teams: List<TeamEntity>) {
        teams.forEach { newTeam ->
            val currentFavoriteStatus = isFavorite(newTeam.id) ?: false
            insertTeam(newTeam.copy(isFavorite = currentFavoriteStatus))
        }
    }

    @Query("UPDATE teams SET isFavorite = :isFavorite WHERE id = :teamId")
    suspend fun updateFavoriteStatus(teamId: String, isFavorite: Boolean)
}