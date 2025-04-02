package com.example.parkour.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.data.model.Competition
import com.example.parkour.data.model.Competitor
import com.example.parkour.viewmodel.ArbitrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("DefaultLocale")
@Composable
fun ArbitrationView(
    viewModel: ArbitrationViewModel,
    navController: NavController,
    competitionId: Int
) {
    val competitions by viewModel.competitions.collectAsState()
    val competitors by viewModel.competitors.collectAsState()
    val obstacles by viewModel.obstacles.collectAsState()

    var elapsedTime by remember { mutableStateOf(0L) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var lastPausedTime by remember { mutableStateOf(0L) }
    var isPaused by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    var selectedCompetition by remember { mutableStateOf<Competition?>(null) }

    var expandedCompetitor by remember { mutableStateOf(false) }
    var selectedCompetitor by remember { mutableStateOf<Competitor?>(null) }



    LaunchedEffect(Unit) {
        viewModel.loadCompetitions()
    }


    LaunchedEffect(competitionId, competitions) {
        selectedCompetition = competitions.find { it.id == competitionId }
        selectedCompetition?.let {
            viewModel.loadCompetitorsForCompetition(it.id)
            // Réinitialiser le compétiteur sélectionné si ce n'est plus dans cette compétition
            if (selectedCompetitor != null && competitors.none { it.id == selectedCompetitor?.id }) {
                selectedCompetitor = null // Réinitialiser si le compétiteur n'est pas dans la nouvelle compétition
            }
        }
    }


    // Charger les compétiteurs lorsque la compétition est sélectionnée
    LaunchedEffect(selectedCompetition) {
        selectedCompetition?.let {
            viewModel.loadCompetitorsForCompetition(it.id)
        }
    }

    LaunchedEffect(isTimerRunning) {
        viewModel.loadCompetitions()
        if (isTimerRunning) {
            viewModel.startTimer { time ->
                elapsedTime = lastPausedTime + time
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Arbitrage", style = MaterialTheme.typography.headlineMedium)

        // Dropdown menu pour sélectionner une compétition
        Text("Sélectionner une compétition")
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedCompetition?.name ?: "Aucune sélection",
                onValueChange = {},
                label = { Text("Compétition") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .clickable { expanded = !expanded } // Ajouter l'événement de clic pour ouvrir le menu
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                competitions.forEach { competition ->
                    DropdownMenuItem(
                        text = { Text(competition.name) },
                        onClick = {
                            selectedCompetition = competition
                            expanded = false
                            viewModel.loadCompetitorsForCompetition(competition.id)
                        }
                    )
                }
            }
        }

        // Sélectionner un compétiteur
        Spacer(modifier = Modifier.height(16.dp))
        if (selectedCompetition != null) {
            Text("Sélectionner un compétiteur")
            ExposedDropdownMenuBox(
                expanded = expandedCompetitor,
                onExpandedChange = { expandedCompetitor = it }
            ) {
                OutlinedTextField(
                    value = selectedCompetitor?.firstName ?: "Aucune sélection",
                    onValueChange = {},
                    label = { Text("Compétiteur") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCompetitor) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .clickable { expandedCompetitor = !expandedCompetitor }
                )

                ExposedDropdownMenu(
                    expanded = expandedCompetitor,
                    onDismissRequest = { expandedCompetitor = false }
                ) {
                    competitors.forEach { competitor ->
                        DropdownMenuItem(
                            text = { Text(competitor.firstName) },
                            onClick = {
                                selectedCompetitor = competitor
                                expandedCompetitor = false
                                viewModel.selectCompetitor(competitor.id)
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Timer
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
            // Start from zero
            Button(onClick = {
                elapsedTime = 0L
                lastPausedTime = 0L
                isPaused = false
                isTimerRunning = true
            }) {
                Text("Démarrer")
            }

            // Pause the timer
            Button(onClick = {
                isTimerRunning = false
                isPaused = true
                lastPausedTime = elapsedTime
                viewModel.stopTimer()
            }) {
                Text("Pause")
            }

            // Resume the timer
            Button(
                onClick = {
                    if (isPaused) {
                        isTimerRunning = true
                        isPaused = false
                    }
                },
                enabled = isPaused
            ) {
                Text("Reprendre")
            }

            // Stop the timer completely
            Button(onClick = {
                isTimerRunning = false
                elapsedTime = 0L
                lastPausedTime = 0L
                isPaused = false
                viewModel.stopTimer()
            }) {
                Text("Arrêter")
            }
        }

        // Chute button (kept as in original code)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.registerPerformance(elapsedTime.toDouble(), hasFell = true)
        }) {
            Text("Chute")
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
