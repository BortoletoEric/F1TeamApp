package io.github.bortoletoeric.f1teamapp.ui.drivers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.bortoletoeric.f1teamapp.domain.model.Driver
import io.github.bortoletoeric.f1teamapp.domain.model.Team
import io.github.bortoletoeric.f1teamapp.domain.repository.TeamRepository
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
    val errorMessage: String? = null
)

class DriversViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: TeamRepository
) : ViewModel() {

    // Assumindo que o ID do time é passado via Navigation
    private val teamId: String = checkNotNull(savedStateHandle["teamId"])

    private val _uiState = MutableStateFlow(DriversUiState(isLoading = true))
    val uiState: StateFlow<DriversUiState> = _uiState.asStateFlow()

    init {
        loadTeamAndDrivers()
    }

    private fun loadTeamAndDrivers() {
        viewModelScope.launch {
            // Combina os dois Flows do Room para emitir um estado unificado
            combine(
                repository.observeTeam(teamId),
                repository.observeDriversByTeam(teamId)
            ) { team, drivers ->
                DriversUiState(team = team, drivers = drivers, isLoading = false)
            }.collect { state ->
                _uiState.update { state }
            }
        }
    }
}