package io.github.bortoletoeric.f1teamapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.bortoletoeric.f1teamapp.data.local.entity.DriverEntity

@Dao
interface DriverDao {
    @Query("SELECT * FROM drivers WHERE teamId = :teamId")
    suspend fun getDriversByTeamId(teamId: String): List<DriverEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrivers(drivers: List<DriverEntity>)

    @Query("DELETE FROM drivers WHERE teamId = :teamId")
    suspend fun deleteDriversByTeamId(teamId: String)
}