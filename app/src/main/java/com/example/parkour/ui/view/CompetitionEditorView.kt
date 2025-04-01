package com.example.parkour.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.ui.viewmodel.CompetitionViewModel
import com.example.parkour.data.model.Competition
import com.example.parkour.ui.items.CompetitorItem
import com.example.parkour.ui.items.CourseItem
import com.example.parkour.viewmodel.CourseViewModel

@Composable
fun CompetitionEditorView(viewModel: CompetitionViewModel, courseViewModel: CourseViewModel, navController: NavController, competitionId: Int) {
    LaunchedEffect(competitionId) {
        viewModel.getCompetitionById(competitionId)
        viewModel.loadCoursesForCompetition(competitionId)
        viewModel.loadCompetitionCompetitors(competitionId)
    }

    val competition by viewModel.selectedCompetition.collectAsState()
    val courses = viewModel.courses.collectAsState().value
    val competitors = viewModel.competitors.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Gestion de la compétition: ${competition?.name ?: "Inconnue"}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 📌 Affichage des courses
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Courses associées :", style = MaterialTheme.typography.bodyLarge)
                Button(onClick = { navController.navigate("createCourse/$competitionId") }) {
                    Text("+")
                }
            }

            if (courses.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    items(courses) { course ->
                        CourseItem(
                            course = course,
                            onEdit = { navController.navigate("updateCourse/${course.id}") },
                            onDelete = {
                                courseViewModel.deleteCourse(course.id)
                                viewModel.loadCoursesForCompetition(competitionId) // 🔄 Actualisation après suppression
                            }
                        )
                    }
                }
            } else {
                Text("Aucune course associée.", style = MaterialTheme.typography.bodyMedium)
            }

            // 📌 Affichage des compétiteurs
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Compétiteurs de la compétition :", style = MaterialTheme.typography.bodyLarge)
            Button(onClick = { navController.navigate("addCompetitorCompetition/$competitionId") }) {
                Text("+")
            }
                }

            if (competitors.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    items(competitors) { competitor ->
                        CompetitorItem(
                            competitor = competitor,
                            isSelected = false,
                            onSelectionChange = {},
                            onDelete = {
                                viewModel.removeCompetitorFromCompetition(competitionId, competitor.id)
                                viewModel.loadCompetitionCompetitors(competitionId) // 🔄 Actualisation après suppression
                            },
                            isAlreadyAdded = true // Les compétiteurs affichés sont déjà ajoutés
                        )
                    }
                }
            } else {
                Text("Aucun compétiteur inscrit.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
