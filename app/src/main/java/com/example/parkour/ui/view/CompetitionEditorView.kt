package com.example.parkour.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.ui.viewmodel.CompetitionViewModel
import com.example.parkour.data.model.Competition
import com.example.parkour.ui.items.CompetitorItem
import com.example.parkour.ui.items.CourseItem
import com.example.parkour.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompetitionEditorView(
    viewModel: CompetitionViewModel,
    courseViewModel: CourseViewModel,
    navController: NavController,
    competitionId: Int
) {
    var coursesChanged by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(competitionId, coursesChanged) {
        viewModel.getCompetitionById(competitionId)
        viewModel.loadCoursesForCompetition(competitionId)
        viewModel.loadCompetitionCompetitors(competitionId)
    }

    val competition by viewModel.selectedCompetition.collectAsState()
    val courses = viewModel.courses.collectAsState().value
    val competitors = viewModel.competitors.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Gestion de la compétition " + (competition?.name ?: "")) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = { navController.navigate("updateCompetition/${competition!!.id}") },
                    modifier = Modifier.weight(1f).padding(8.dp)
                ) {
                    Text("Modifier")
                }
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.weight(1f).padding(8.dp),
                ) {
                    Text("Supprimer")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 📌 Affichage des courses
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Courses associées :", style = MaterialTheme.typography.bodyLarge)
                Button(onClick = { navController.navigate("createCourse/$competitionId") }) {
                    Text("+")
                }
            }

            if (courses.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).height(300.dp)) {
                    items(courses) { course ->
                        CourseItem(
                            course = course,
                            onEdit = { navController.navigate("updateCourse/${course.id}") },
                            onDelete = {
                                courseViewModel.deleteCourse(course.id, competitionId)
                                coursesChanged = !coursesChanged
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
                LazyColumn(modifier = Modifier.fillMaxWidth().padding(8.dp).size(250.dp)) {
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

    // 📌 Boîte de dialogue pour confirmer la suppression
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmer la suppression") },
            text = { Text("Voulez-vous vraiment supprimer cette compétition ?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCompetition(competition!!.id)
                        navController.popBackStack()
                        showDialog = false
                    }
                ) {
                    Text("Oui", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}
