package com.example.parkour.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.data.model.create.CourseCreate
import com.example.parkour.ui.viewmodel.CompetitionViewModel

@Composable
fun CreateCourseView(viewModel: CompetitionViewModel, navController: NavController, competitionId: Int) {
    var name by remember { mutableStateOf("") }
    var maxDuration by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Créer un parcours", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nom du parcours") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = maxDuration,
            onValueChange = { maxDuration = it.filter { char -> char.isDigit() } },
            label = { Text("Durée maximale (secondes)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val course = CourseCreate(
                    name = name,
                    maxDuration = maxDuration.toIntOrNull() ?: 0,
                    competitionId = competitionId
                )
                viewModel.addCourse(course)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ajouter la course")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Annuler")
        }
    }
}

