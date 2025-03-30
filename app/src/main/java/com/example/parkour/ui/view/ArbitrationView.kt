package com.example.parkour.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.viewmodel.ArbitrationViewModel

@SuppressLint("DefaultLocale")
@Composable
fun ArbitrationView(
    viewModel: ArbitrationViewModel,
    navController: NavController
) {
    val competitions by viewModel.competitions.collectAsState()
    val competitors by viewModel.competitors.collectAsState()
    val obstacles by viewModel.obstacles.collectAsState()
    var elapsedTime by remember { mutableStateOf(0L) }
    var isTimerRunning by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Arbitrage", style = MaterialTheme.typography.headlineMedium)

        // Sélectionner la compétition
        Text("Sélectionner une compétition")
        LazyColumn {
            items(competitions) { competition ->
                Button(onClick = {
                    viewModel.loadCompetitors()
                    viewModel.selectCourse(competition.id)
                }) {
                    Text("Compétition: ${competition.name}")
                }
            }
        }

        // Sélectionner un compétiteur
        Spacer(modifier = Modifier.height(16.dp))
        Text("Sélectionner un compétiteur")
        LazyColumn {
            items(competitors) { competitor ->
                Button(onClick = { viewModel.selectCompetitor(competitor.id) }) {
                    Text("Compétiteur: ${competitor.firstName}")
                }
            }
        }

        // Timer
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = String.format(
                "Temps écoulé : %02d:%02d.%02d",
                elapsedTime / 60000,
                (elapsedTime / 1000) % 60,
                (elapsedTime % 1000) / 10
            ),
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                isTimerRunning = true
                viewModel.startTimer { time -> elapsedTime = time }
            }) {
                Text("Démarrer")
            }
            Button(onClick = {
                isTimerRunning = false
                viewModel.stopTimer()
            }) {
                Text("Arrêter")
            }
            Button(onClick = {
                viewModel.registerPerformance(elapsedTime.toDouble(), hasFell = true)
            }) {
                Text("Chute")
            }
        }

        // Obstacle suivant
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.moveToNextObstacle() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Obstacle suivant")
        }

        // Liste des obstacles
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Liste des obstacles", style = MaterialTheme.typography.bodyLarge)
        obstacles.forEach { obstacle ->
            Text(text = "- Obstacle ID: ${obstacle.id}")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Retour")
        }
    }
}
