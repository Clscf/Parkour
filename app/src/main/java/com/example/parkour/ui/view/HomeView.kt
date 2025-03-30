package com.example.parkour

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.ui.viewmodel.CompetitionViewModel
import com.example.parkour.viewmodel.CourseViewModel
import com.example.parkour.ui.items.CourseItem
import androidx.compose.runtime.collectAsState
import com.example.parkour.data.model.Competition

@Composable
fun HomeView(viewModel: CompetitionViewModel, courseViewModel: CourseViewModel, navController: NavController) {
    val competitions = viewModel.competitions.collectAsState().value
    val courses = viewModel.courses.collectAsState().value
    val competitionDeleted = viewModel.competitionDeleted.collectAsState().value
    var expanded by remember { mutableStateOf(false) }
    var selectedCompetition by remember { mutableStateOf<Competition?>(null) }

    if (competitionDeleted) {
        selectedCompetition = null
        viewModel.resetDeletionState()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
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
                                viewModel.loadCoursesForCompetition(competition.id)
                                expanded = false
                            }
                        )
                    }
                }

                selectedCompetition?.let { competition ->
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Courses associées :",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Button(
                                onClick = { navController.navigate("createCourse/${competition.id}") },
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Text("+")
                            }
                        }

                        if (courses.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth().padding(8.dp)
                            ) {
                                items(courses) { course ->
                                    CourseItem(
                                        course = course,
                                        onEdit = { navController.navigate("updateCourse/${course.id}") },
                                        onDelete = { courseViewModel.deleteCourse(course.id) }
                                    )
                                }
                            }
                        } else {
                            Text("Aucune course associée.", style = MaterialTheme.typography.bodyMedium)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { navController.navigate("updateCompetition/${competition.id}") },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text("Modifier")
                            }
                            Button(
                                onClick = { viewModel.deleteCompetition(competition.id) },
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
