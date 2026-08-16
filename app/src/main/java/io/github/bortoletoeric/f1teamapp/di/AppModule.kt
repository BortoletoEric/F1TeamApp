package io.github.bortoletoeric.f1teamapp.di

import androidx.room.Room
import io.github.bortoletoeric.f1teamapp.data.local.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import io.github.bortoletoeric.f1teamapp.data.remote.F1ApiService
import io.github.bortoletoeric.f1teamapp.data.repository.TeamRepositoryImpl
import io.github.bortoletoeric.f1teamapp.domain.repository.TeamRepository
import io.github.bortoletoeric.f1teamapp.ui.drivers.DriversViewModel
import io.github.bortoletoeric.f1teamapp.ui.teams.TeamsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import retrofit2.converter.gson.GsonConverterFactory

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
        Retrofit.Builder()
            .baseUrl("https://f1api.dev/pt/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(F1ApiService::class.java)
    }

    // 3. Instância do Repositório (Injenta API e DAO automaticamente)
    single<TeamRepository> { TeamRepositoryImpl(apiService = get(), teamDao = get()) }

    // 4. Instâncias das ViewModels
    viewModel { TeamsViewModel(repository = get()) }
    viewModel { params -> DriversViewModel(savedStateHandle = params.get(), repository = get()) }
}