package io.github.bortoletoeric.f1teamapp.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.bortoletoeric.f1teamapp.domain.model.Team
import io.github.bortoletoeric.f1teamapp.domain.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TeamsUiState(
    val isLoading: Boolean = false,
    val teams: List<Team> = emptyList(),
    val errorMessage: String? = null
)

class TeamsViewModel(
    private val repository: TeamRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamsUiState(isLoading = true))
    val uiState: StateFlow<TeamsUiState> = _uiState.asStateFlow()

    init {
        observeTeams()
        syncTeams()
    }

    private fun observeTeams() {
        viewModelScope.launch {
            repository.observeTeams()
                .catch { e ->
                    _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
                }
                .collect { teams ->
                    _uiState.update { it.copy(teams = teams, isLoading = false) }
                }
        }
    }

    private fun syncTeams() {
        viewModelScope.launch {
            try {
                repository.syncData()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Erro de sincronização: ${e.message}") }
            }
        }
    }

    fun onToggleFavorite(teamId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(teamId, !currentStatus)
        }
    }
}