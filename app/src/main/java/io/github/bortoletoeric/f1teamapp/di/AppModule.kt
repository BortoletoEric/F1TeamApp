package io.github.bortoletoeric.f1teamapp.di

import androidx.room.Room
import io.github.bortoletoeric.f1teamapp.data.local.AppDatabase
import io.github.bortoletoeric.f1teamapp.data.remote.F1ApiService
import io.github.bortoletoeric.f1teamapp.data.repository.TeamRepositoryImpl
import io.github.bortoletoeric.f1teamapp.domain.repository.TeamRepository
import io.github.bortoletoeric.f1teamapp.ui.drivers.DriversViewModel
import io.github.bortoletoeric.f1teamapp.ui.teams.TeamsViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val appModule = module {
    // 1. Instância do Room Database & DAO
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "f1_db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().teamDao() }

    // 2. Instância do Retrofit & API Service
    single {
        val json = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .baseUrl("https://f1api.dev/pt/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(F1ApiService::class.java)
    }

    // 3. Instância do Repositório (Injeta API e DAO automaticamente)
    single<TeamRepository> { TeamRepositoryImpl(apiService = get(), dao = get()) }

    // 4. Instâncias das ViewModels
    viewModel { TeamsViewModel(teamRepository = get()) }
    viewModel { DriversViewModel(teamRepository = get()) }
}