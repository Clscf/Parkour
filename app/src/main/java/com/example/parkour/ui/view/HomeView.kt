package com.example.parkour

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.parkour.viewmodel.CourseViewModel
import com.example.parkour.ui.items.CourseItem
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.example.parkour.data.model.Competition


@Composable
fun SimpleBottomNavigation(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icône Accueil
        IconButton(onClick = { navController.navigate("home") }) {
            Column(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Accueil",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Accueil",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
            }
        }

        // Icône Créer
        IconButton(onClick = { navController.navigate("createCompetition") }) {
            Column(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Créer",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Créer",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
            }
        }

        // Icône Arbitrage
        IconButton(onClick = { navController.navigate("arbitration/1/1") }) {
            Column(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Arbitrage",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Arbitrage",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun HomeView(viewModel: CompetitionViewModel, courseViewModel: CourseViewModel, navController: NavController) {
    val competitions = viewModel.competitions.collectAsState().value
    val courses = viewModel.courses.collectAsState().value
    val competitionDeleted = viewModel.competitionDeleted.collectAsState().value
    var expanded by remember { mutableStateOf(false) }
    var selectedCompetition by remember { mutableStateOf<Competition?>(null) }
    val competitors = viewModel.competitors.collectAsState().value


    // Réinitialisation automatique après suppression
    if (competitionDeleted) {
        selectedCompetition = null
        viewModel.resetDeletionState()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { SimpleBottomNavigation(navController = navController) } // Barre de navigation en bas
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp), // Ajout d'un padding général pour éviter que les éléments touchent les bords
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Compétitions disponibles",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp) // Un peu d'espace sous le titre
            )

            if (competitions.isEmpty()) {
                // Afficher si aucune compétition n'est disponible
                Text("Aucune compétition disponible.", modifier = Modifier.padding(bottom = 16.dp))
            } else {
                OutlinedTextField(
                    value = selectedCompetition?.name ?: "Sélectionner une compétition",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp) // Espacement sous le champ de sélection
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
                                viewModel.loadCompetitionCompetitors(competition.id)
                                expanded = false
                            }
                        )
                    }
                }

                selectedCompetition?.let { competition ->
                    // Affichage de la compétition sélectionnée
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween // Mise en page propre des éléments
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp) // Ajout d'un espacement en bas de la liste
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
                            Text(
                                "Aucune course associée.",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        // Section des boutons modifier, supprimer et arbitrer
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { navController.navigate("updateCompetition/${competition.id}") },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("Modifier")
                            }
                            Button(
                                onClick = { viewModel.deleteCompetition(competition.id) },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("Supprimer")
                            }
                            Button(
                                onClick = { navController.navigate("arbitration/${competition.id}/1") },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("Arbitrer")
                            }
                        }
                    }
                }
            }

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth()) {
                        competitions.forEach { competition ->
                            DropdownMenuItem(
                                text = { Text(competition.name) },
                                onClick = {
                                    selectedCompetition = competition
                                    viewModel.loadCoursesForCompetition(competition.id)
                                    viewModel.loadCompetitionCompetitors(competition.id)
                                    expanded = false
                                }
                            )
                        }
                    }

                    selectedCompetition?.let { competition ->
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Text("Compétiteurs de la compétition :", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 8.dp))
                            Button(onClick = { navController.navigate("addCompetitorCompetition/${competition.id}") }, modifier = Modifier.padding(bottom = 8.dp)) {
                                Text("Ajouter un compétiteur")

                            }

                            if (competitors.isNotEmpty()) {
                                LazyColumn(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                    items(competitors) { competitor ->
                                        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text(competitor.firstName, style = MaterialTheme.typography.bodyMedium)
                                            Button(onClick = { viewModel.removeCompetitorFromCompetition(competition.id, competitor.id)  }) {
                                                Text("Supprimer")
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text("Aucun compétiteur inscrit.", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(1f)) // Pour pousser les éléments vers le haut

            Button(
                onClick = { navController.navigate("createCompetition") },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Créer une compétition")
            }
        }
    }
}




