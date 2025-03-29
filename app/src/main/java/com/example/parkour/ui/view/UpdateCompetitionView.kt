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
import com.example.parkour.data.model.CompetitionUpdate
import com.example.parkour.ui.viewmodel.CompetitionViewModel

@Composable
fun UpdateCompetitionView(
    viewModel: CompetitionViewModel,
    navController: NavController,
    competition: CompetitionUpdate
) {
    var name by remember { mutableStateOf(competition.name) }
    var ageMin by remember { mutableStateOf(competition.ageMin) }
    var ageMax by remember { mutableStateOf(competition.ageMax) }
    var gender by remember { mutableStateOf(competition.gender) }
    var expandedGender by remember { mutableStateOf(false) }
    var hasRetry by remember { mutableStateOf(competition.hasRetry) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(if (competition.hasRetry == 1) "Oui" else "Non") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
                        selectedOption = "Oui"
                        hasRetry = 1
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Non") },
                    onClick = {
                        selectedOption = "Non"
                        hasRetry = 0
                        expanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val updatedCompetition = competition.copy(
                    name = name,
                    ageMin = ageMin,
                    ageMax = ageMax,
                    gender = gender,
                    hasRetry = hasRetry
                )
                viewModel.updateCompetition(updatedCompetition)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mettre à jour la compétition")
        }

        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Annuler")
        }
    }
}
