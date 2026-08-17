package io.github.bortoletoeric.f1teamapp.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.bortoletoeric.f1teamapp.domain.model.Team
import io.github.bortoletoeric.f1teamapp.domain.repository.TeamRepository
import io.github.bortoletoeric.f1teamapp.util.NetworkError
import io.github.bortoletoeric.f1teamapp.util.toNetworkError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TeamsUiState(
    val isLoading: Boolean = false,
    val teams: List<Team> = emptyList(),
    val error: NetworkError? = null
)

class TeamsViewModel(private val teamRepository: TeamRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamsUiState(isLoading = true))
    val uiState: StateFlow<TeamsUiState> = _uiState.asStateFlow()

    init {
        observeTeams()
        refresh()
    }

    private fun observeTeams() {
        // SSOT: Observa as mudanças do banco local
        viewModelScope.launch {
            teamRepository.getTeams().collect { teams ->
                _uiState.update { it.copy(teams = teams, isLoading = false) }
            }
        }
    }

    fun refresh() {
        // Atualiza o banco com os dados da API
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                teamRepository.syncTeams()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.toNetworkError(), isLoading = false) }
            }
        }
    }

    fun toggleFavorite(teamId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            // Envia o novo estado de favorito para o repositório
            teamRepository.toggleFavorite(teamId, !isFavorite)
        }
    }
}