package io.github.bortoletoeric.f1teamapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.bortoletoeric.f1teamapp.data.local.dao.TeamDao
import io.github.bortoletoeric.f1teamapp.data.local.dao.DriverDao
import io.github.bortoletoeric.f1teamapp.data.local.entity.TeamEntity
import io.github.bortoletoeric.f1teamapp.data.local.entity.DriverEntity

@Database(entities = [TeamEntity::class, DriverEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teamDao(): TeamDao
    abstract fun driverDao(): DriverDao
}