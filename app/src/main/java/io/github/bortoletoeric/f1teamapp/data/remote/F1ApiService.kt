package io.github.bortoletoeric.f1teamapp.data.remote

import io.github.bortoletoeric.f1teamapp.data.remote.dto.DriverResponse
import io.github.bortoletoeric.f1teamapp.data.remote.dto.TeamResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface F1ApiService {
    @GET("api/current/teams")
    suspend fun getTeams(): TeamResponse

    @GET("api/current/teams/{teamId}/drivers")
    suspend fun getDriversByTeam(
        @Path("teamId") teamId: String
    ): DriverResponse // Crie a classe DriverResponse mapeando a estrutura JSON de pilotos
}