package io.github.bortoletoeric.f1teamapp.ui.drivers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.bortoletoeric.f1teamapp.domain.model.Driver
import io.github.bortoletoeric.f1teamapp.domain.model.Team
import io.github.bortoletoeric.f1teamapp.domain.repository.TeamRepository
import io.github.bortoletoeric.f1teamapp.util.NetworkError
import io.github.bortoletoeric.f1teamapp.util.toNetworkError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DriversUiState(
    val isLoading: Boolean = false,
    val team: Team? = null,
    val drivers: List<Driver> = emptyList(),
    val error: NetworkError? = null
)

class DriversViewModel(
    private val teamRepository: TeamRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DriversUiState())
    val uiState: StateFlow<DriversUiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("teamId")?.let { teamId ->
            loadDrivers(teamId)
        }
    }

    private fun loadDrivers(teamId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Requer um metodo no TeamRepository para buscar um único time pelo ID
                val currentTeam = teamRepository.getTeamById(teamId)

                val drivers = teamRepository.getTeamDrivers(teamId)
                val sortedDrivers = drivers.sortedByDescending { it.points }

                _uiState.update {
                    it.copy(team = currentTeam, drivers = sortedDrivers, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toNetworkError(), isLoading = false) }
            }
        }
    }

    fun retry() {
        val teamId = _uiState.value.team?.id ?: return
        loadDrivers(teamId)
    }
}