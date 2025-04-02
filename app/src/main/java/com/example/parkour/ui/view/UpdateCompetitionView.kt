package com.example.parkour.ui.view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.data.model.uptdate.CompetitionUpdate
import com.example.parkour.ui.viewmodel.CompetitionViewModel

@Composable
fun UpdateCompetitionView(
    viewModel: CompetitionViewModel,
    navController: NavController,
    competition: CompetitionUpdate,
    competitionId: Int
) {
    var name by remember { mutableStateOf(competition.name) }
    var ageMin by remember { mutableStateOf(competition.ageMin) }
    var ageMax by remember { mutableStateOf(competition.ageMax) }
    var gender by remember { mutableStateOf(competition.gender) }
    var expandedGender by remember { mutableStateOf(false) }
    var hasRetry by remember { mutableStateOf(competition.hasRetry) }
    var expanded by remember { mutableStateOf(false) }
    val selectedOption = if (hasRetry == 1) "Oui" else "Non"

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = {
                        val updatedCompetition = CompetitionUpdate(
                            name = name,
                            ageMin = ageMin,
                            ageMax = ageMax,
                            gender = gender,
                            hasRetry = hasRetry,
                            status = competition.status
                        )
                        viewModel.updateCompetition(competitionId, updatedCompetition)
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text("Mettre à jour")
                }

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text("Annuler")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = paddingValues.calculateBottomPadding()), // Ajuste la hauteur du contenu
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Modifier la compétition", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom de la compétition") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = ageMin.toString(),
                onValueChange = { ageMin = it.toIntOrNull() ?: competition.ageMin },
                label = { Text("Âge minimum") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = ageMax.toString(),
                onValueChange = { ageMax = it.toIntOrNull() ?: competition.ageMax },
                label = { Text("Âge maximum") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = gender,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                        .padding(16.dp)
                        .clickable { expandedGender = true }
                )
                DropdownMenu(
                    expanded = expandedGender,
                    onDismissRequest = { expandedGender = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Homme") },
                        onClick = {
                            gender = "H"
                            expandedGender = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Femme") },
                        onClick = {
                            gender = "F"
                            expandedGender = false
                        }
                    )
                }
            }

            Box(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = selectedOption,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray)
                        .padding(16.dp)
                        .clickable { expanded = true }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Oui") },
                        onClick = {
                            hasRetry = 1
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Non") },
                        onClick = {
                            hasRetry = 0
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
