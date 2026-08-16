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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.github.bortoletoeric.f1teamapp.domain.model.Team

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
    if (uiState.isLoading && uiState.teams.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(uiState.teams, key = { it.id }) { team ->
            TeamItem(
                team = team,
                onClick = { onTeamClick(team.id) },
                onToggleFavorite = { onToggleFavorite(team.id, team.isFavorite) }
            )
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
            AsyncImage(
                model = team.logoUrl,
                contentDescription = "Logo ${team.name}",
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = team.name, style = MaterialTheme.typography.titleMedium)
                Text(text = team.description, style = MaterialTheme.typography.bodyMedium)
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