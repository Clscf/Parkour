package com.example.parkour.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.data.model.CompetitionCreate
import com.example.parkour.ui.viewmodel.CompetitionViewModel

@Composable
fun CreateCompetitionView(
    viewModel: CompetitionViewModel,
    navController: NavController
) {

    val name = remember { mutableStateOf("") }
    val ageMin = remember { mutableStateOf(18) }
    val ageMax = remember { mutableStateOf(30) }
    val gender = remember { mutableStateOf('M') }
    val hasTry = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Créer une compétition", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))


        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Nom de la compétition") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        TextField(
            value = ageMin.value.toString(),
            onValueChange = { ageMin.value = it.toIntOrNull() ?: 18 },
            label = { Text("Âge minimum") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        TextField(
            value = ageMax.value.toString(),
            onValueChange = { ageMax.value = it.toIntOrNull() ?: 30 },
            label = { Text("Âge maximum") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        TextField(
            value = gender.value.toString(),
            onValueChange = { gender.value = it.firstOrNull() ?: 'M' },
            label = { Text("Genre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = hasTry.value.toString(),
            onValueChange = { hasTry.value = it.toIntOrNull() ?: 0 },
            label = { Text("Nombre d'essais") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val competition = CompetitionCreate(
                    name = name.value,
                    ageMin = ageMin.value,
                    ageMax = ageMax.value,
                    gender = gender.value,
                    hasRetry = hasTry.value
                )
                viewModel.addCompetition(competition)

                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Créer la compétition")
        }
    }
}
