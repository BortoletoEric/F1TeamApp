package io.github.bortoletoeric.f1teamapp.data.remote

import io.github.bortoletoeric.f1teamapp.data.remote.dto.TeamDto
import retrofit2.http.GET

interface F1ApiService {
    @GET("teams/current-teams")
    suspend fun getCurrentTeams(): List<TeamDto>
}