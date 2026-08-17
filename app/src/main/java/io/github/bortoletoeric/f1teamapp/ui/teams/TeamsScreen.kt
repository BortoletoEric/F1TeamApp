package io.github.bortoletoeric.f1teamapp.ui.teams

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.bortoletoeric.f1teamapp.domain.model.Team
import io.github.bortoletoeric.f1teamapp.ui.components.ErrorState
import kotlinx.coroutines.launch

@Composable
fun TeamsRoute(
    viewModel: TeamsViewModel,
    onNavigateToTeamDetails: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeamsScreen(
        uiState = uiState,
        onTeamClick = onNavigateToTeamDetails,
        onToggleFavorite = viewModel::toggleFavorite,
        onRetry = viewModel::refresh
    )
}

@Composable
fun TeamsScreen(
    uiState: TeamsUiState,
    onTeamClick: (String) -> Unit,
    onToggleFavorite: (String, Boolean) -> Unit,
    onRetry: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Efeito para mostrar erro via Snackbar quando já temos dados em cache
    LaunchedEffect(uiState.error) {
        if (uiState.error != null && uiState.teams.isNotEmpty()) {
            snackbarHostState.showSnackbar(
                message = uiState.error.toMessage(),
                actionLabel = "Tentar novamente"
            ).also { result ->
                if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                    onRetry()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading && uiState.teams.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null && uiState.teams.isEmpty()) {
                ErrorState(
                    error = uiState.error,
                    onRetry = onRetry
                )
            } else if (uiState.teams.isEmpty() && !uiState.isLoading) {
                Text(
                    text = "Nenhum time encontrado.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.teams, key = { it.id }) { team ->
                        TeamItem(
                            team = team,
                            onClick = { onTeamClick(team.id) },
                            onToggleFavorite = {
                                onToggleFavorite(team.id, team.isFavorite)
                                val message = if (team.isFavorite) {
                                    "Removido dos favoritos: ${team.name}"
                                } else {
                                    "Adicionado aos favoritos: ${team.name}"
                                }
                                scope.launch {
                                    snackbarHostState.showSnackbar(message)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TeamItem(
    team: Team,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Exibindo a posição do time no campeonato
            Text(
                text = "${team.position}º",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = team.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Temporada ${team.season} | ${team.points} pts | ${team.wins} vitórias",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (team.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favoritar",
                    tint = if (team.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Teams List - Success")
@Composable
fun TeamsScreenSuccessPreview() {
    MaterialTheme {
        TeamsScreen(
            uiState = TeamsUiState(
                isLoading = false,
                teams = listOf(
                    Team(
                        id = "red_bull",
                        name = "Red Bull Racing",
                        position = 1,
                        points = 860.0f,
                        wins = 21,
                        season = 2024,
                        isFavorite = true
                    ),
                    Team(
                        id = "mercedes",
                        name = "Mercedes",
                        position = 2,
                        points = 409.0f,
                        wins = 0,
                        season = 2024,
                        isFavorite = false
                    )
                )
            ),
            onTeamClick = {},
            onToggleFavorite = { _, _ -> },
            onRetry = {}
        )
    }
}