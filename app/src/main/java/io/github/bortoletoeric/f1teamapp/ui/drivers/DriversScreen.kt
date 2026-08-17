package io.github.bortoletoeric.f1teamapp.ui.drivers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.github.bortoletoeric.f1teamapp.domain.model.Driver
import io.github.bortoletoeric.f1teamapp.domain.model.Team

@Composable
fun DriversRoute(
    viewModel: DriversViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DriversScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun DriversScreen(
    uiState: DriversUiState,
    onNavigateBack: () -> Unit
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.errorMessage != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onNavigateBack) {
                        Text("Voltar")
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    uiState.team?.let { team ->
                        TeamHeader(
                            team = team,
                            onBack = onNavigateBack
                        )
                    }

                    if (uiState.drivers.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Nenhum piloto encontrado para esta equipe.")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.drivers, key = { it.id }) { driver ->
                                DriverItem(driver = driver)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeamHeader(
    team: Team,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onBack, modifier = Modifier.align(Alignment.Start)) {
            Text("Voltar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Se estiver usando a URL da API para os times, pode trocar o Icon pelo AsyncImage no futuro
        Icon(
            imageVector = Icons.Default.Groups,
            contentDescription = "Team Icon",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = team.name, style = MaterialTheme.typography.headlineMedium)
        Text(text = "Posição: ${team.position}º lugar", style = MaterialTheme.typography.bodyLarge)
        Text(
            text = "${team.points} Pontos | ${team.wins} Vitórias",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun DriverItem(driver: Driver) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Posição do piloto
            Text(
                text = "${driver.position}º",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(end = 12.dp)
            )

            AsyncImage(
                model = driver.photoUrl,
                contentDescription = "Foto ${driver.fullName}",
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${driver.fullName} #${driver.number ?: ""}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${driver.nationality} | ${driver.age} anos",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${driver.points} pts",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
