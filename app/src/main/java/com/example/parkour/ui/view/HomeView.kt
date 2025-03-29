package com.example.parkour

import CompetitionItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.ui.viewmodel.CompetitionViewModel
import androidx.compose.runtime.collectAsState
import com.example.parkour.data.model.Competition

@Composable
fun HomeView(viewModel: CompetitionViewModel, navController: NavController) {
    val competitions = viewModel.competitions.collectAsState().value
    val competitionDeleted = viewModel.competitionDeleted.collectAsState().value
    var expanded by remember { mutableStateOf(false) }
    var selectedCompetition by remember { mutableStateOf<Competition?>(null) }

    // Réinitialisation automatique après suppression
    if (competitionDeleted) {
        selectedCompetition = null
        viewModel.resetDeletionState() // Reset après traitement
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Compétitions disponibles",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )

            if (competitions.isEmpty()) {
                Text("Aucune compétition disponible.")
            } else {
                OutlinedTextField(
                    value = selectedCompetition?.name ?: "Sélectionner une compétition",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { expanded = true },
                    readOnly = true
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    competitions.forEach { competition ->
                        DropdownMenuItem(
                            text = { Text(competition.name) },
                            onClick = {
                                selectedCompetition = competition
                                viewModel.loadCompetitions()
                                expanded = false
                            }
                        )
                    }
                }

                selectedCompetition?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Compétition sélectionnée : ${it.name}")

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { navController.navigate("updateCompetition/${it.id}") },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text("Modifier")
                            }
                            Button(
                                onClick = { viewModel.deleteCompetition(it.id) },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text("Supprimer")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate("createCompetition") },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Créer une compétition")
            }
        }
    }
}



