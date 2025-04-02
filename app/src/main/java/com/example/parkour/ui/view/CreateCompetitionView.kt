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
import com.example.parkour.data.model.create.CompetitionCreate
import com.example.parkour.ui.viewmodel.CompetitionViewModel

@Composable
fun CreateCompetitionView(
    viewModel: CompetitionViewModel,
    navController: NavController
) {
    val competitions by viewModel.competitions.collectAsState()

    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var ageMin by remember { mutableStateOf("") }
    var ageMax by remember { mutableStateOf("") }
    var ageError by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf("Genre") }
    var expandedGender by remember { mutableStateOf(false) }
    var hasTry by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Essai") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Créer une compétition", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = competitions.any { competition -> competition.name == it }
            },
            label = { Text("Nom de la compétition") },
            modifier = Modifier.fillMaxWidth(),
            isError = nameError
        )

        if (nameError) {
            Text(
                text = "Ce nom est déjà pris !",
                color = Color.Red,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = ageMin,
            onValueChange = {
                ageMin = it.filter { char -> char.isDigit() }
                ageError = (ageMin.toIntOrNull() ?: -1) >= (ageMax.toIntOrNull() ?: Int.MAX_VALUE)
            },
            label = { Text("Âge minimum") },
            modifier = Modifier.fillMaxWidth(),
            isError = ageError
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = ageMax,
            onValueChange = {
                ageMax = it.filter { char -> char.isDigit() }
                ageError = (ageMin.toIntOrNull() ?: -1) >= (ageMax.toIntOrNull() ?: Int.MAX_VALUE)
            },
            label = { Text("Âge maximum") },
            modifier = Modifier.fillMaxWidth(),
            isError = ageError
        )

        if (ageError) {
            Text(
                text = "L'âge minimum doit être inférieur à l'âge maximum",
                color = Color.Red,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }

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
                        selectedOption = "true"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Non") },
                    onClick = {
                        selectedOption = "false"
                        expanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val competition = CompetitionCreate(
                    name = name,
                    ageMin = ageMin.toIntOrNull() ?: 0,
                    ageMax = ageMax.toIntOrNull() ?: 0,
                    gender = gender,
                    hasRetry = hasTry
                )
                viewModel.addCompetition(competition)
                navController.popBackStack()
            },
            enabled = name.isNotBlank() && !nameError &&
                    ageMin.toIntOrNull() != null && ageMax.toIntOrNull() != null && !ageError,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Créer la compétition")
        }

        Button(onClick = { navController.popBackStack() }) {
            Text("Retour")
        }
    }
}