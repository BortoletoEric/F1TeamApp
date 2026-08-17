package io.github.bortoletoeric.f1teamapp.data.remote

import io.github.bortoletoeric.f1teamapp.data.remote.dto.ConstructorsChampionshipResponseDto
import io.github.bortoletoeric.f1teamapp.data.remote.dto.DriversResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface F1ApiService {
    @GET("current/constructors-championship")
    suspend fun getConstructorsStandings(): ConstructorsChampionshipResponseDto

    @GET("current/teams/{teamId}/drivers")
    suspend fun getTeamDrivers(@Path("teamId") teamId: String): DriversResponseDto
}