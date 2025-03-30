package com.example.parkour.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
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
    navController: NavController,
    competitionId: Int,
    courseId: Int
) {
    val obstacles by viewModel.obstacles.collectAsState() // Liste des obstacles du parcours
    var elapsedTime by remember { mutableStateOf(0L) } // Chronométrage
    var isTimerRunning by remember { mutableStateOf(false) } // État du chronomètre

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Arbitrage du parcours $courseId", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Timer Display
        Text(
            text = String.format(
                "Temps écoulé : %02d:%02d.%02d",
                elapsedTime / 60000,
                (elapsedTime / 1000) % 60,
                (elapsedTime % 1000) / 10
            ),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Control Buttons
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
                viewModel.registerPerformance(
                    elapsedTime.toDouble(),
                    hasFell = true // Enregistre une chute
                )
            }) {
                Text("Chute")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Obstacle navigation
        Button(
            onClick = {
                viewModel.moveToNextObstacle()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Obstacle suivant")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display List of Obstacles
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