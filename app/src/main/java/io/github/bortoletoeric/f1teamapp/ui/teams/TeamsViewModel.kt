package io.github.bortoletoeric.f1teamapp.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.bortoletoeric.f1teamapp.domain.model.Team
import io.github.bortoletoeric.f1teamapp.domain.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TeamsUiState(
    val isLoading: Boolean = false,
    val teams: List<Team> = emptyList(),
    val errorMessage: String? = null
)

class TeamsViewModel(private val teamRepository: TeamRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TeamsUiState(isLoading = true))
    val uiState: StateFlow<TeamsUiState> = _uiState.asStateFlow()

    init {
        // SSOT: Observa as mudanças do banco local
        viewModelScope.launch {
            teamRepository.getTeams().collect { teams ->
                _uiState.update { it.copy(teams = teams, isLoading = false) }
            }
        }

        // Atualiza o banco com os dados da API
        viewModelScope.launch {
            try {
                teamRepository.syncTeams()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
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