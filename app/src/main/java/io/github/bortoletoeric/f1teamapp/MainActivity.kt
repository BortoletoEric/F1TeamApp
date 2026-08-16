package io.github.bortoletoeric.f1teamapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.bortoletoeric.f1teamapp.ui.drivers.DriversRoute
import io.github.bortoletoeric.f1teamapp.ui.drivers.DriversViewModel
import io.github.bortoletoeric.f1teamapp.ui.teams.TeamsRoute
import io.github.bortoletoeric.f1teamapp.ui.teams.TeamsViewModel
import io.github.bortoletoeric.f1teamapp.ui.theme.F1TeamAppTheme
import io.github.bortoletoeric.f1teamapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinApplication(application = {
                androidContext(this@MainActivity.applicationContext)
                modules(appModule)
            }) {
                F1TeamAppTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()

                        NavHost(navController = navController, startDestination = "teams") {

                            composable("teams") {
                                // Koin injeta o repository automaticamente
                                val viewModel: TeamsViewModel = koinViewModel()

                                TeamsRoute(
                                    viewModel = viewModel,
                                    onNavigateToTeamDetails = { teamId ->
                                        navController.navigate("drivers/$teamId")
                                    }
                                )
                            }

                            composable(
                                route = "drivers/{teamId}",
                                arguments = listOf(navArgument("teamId") { type = NavType.StringType })
                            ) {
                                // Koin passa automaticamente o bundle/argumentos para o SavedStateHandle
                                val viewModel: DriversViewModel = koinViewModel()

                                DriversRoute(
                                    viewModel = viewModel,
                                    onNavigateBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
