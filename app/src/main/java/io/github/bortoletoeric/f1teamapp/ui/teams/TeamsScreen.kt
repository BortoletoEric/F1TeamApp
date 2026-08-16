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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.bortoletoeric.f1teamapp.domain.model.Team
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
        onToggleFavorite = viewModel::onToggleFavorite
    )
}

@Composable
fun TeamsScreen(
    uiState: TeamsUiState,
    onTeamClick: (String) -> Unit,
    onToggleFavorite: (String, Boolean) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
            } else if (uiState.errorMessage != null && uiState.teams.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else if (uiState.teams.isEmpty()) {
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
            Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = "Team Icon",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = team.name, style = MaterialTheme.typography.titleMedium)
                Text(text = team.nationality, style = MaterialTheme.typography.bodyMedium)
            }

            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (team.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favoritar"
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
                        id = "1",
                        name = "Red Bull Racing",
                        nationality = "Austria",
                        firstAppeareance = 2005,
                        constructorsChampionships = 6,
                        driversChampionships = 7,
                        wikipediaUrl = "",
                        isFavorite = true
                    ),
                    Team(
                        id = "2",
                        name = "Ferrari",
                        nationality = "Italy",
                        firstAppeareance = 1950,
                        constructorsChampionships = 16,
                        driversChampionships = 15,
                        wikipediaUrl = "",
                        isFavorite = false
                    )
                )
            ),
            onTeamClick = {},
            onToggleFavorite = { _, _ -> }
        )
    }
}