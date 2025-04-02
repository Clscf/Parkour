package com.example.parkour.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun CompetitionEditorView(viewModel: CompetitionViewModel, courseViewModel: CourseViewModel, navController: NavController, competitionId: Int) {

    var coursesChanged by remember { mutableStateOf(false) }

    LaunchedEffect(competitionId, coursesChanged) {
        viewModel.getCompetitionById(competitionId)
        viewModel.loadCoursesForCompetition(competitionId)
        viewModel.loadCompetitionCompetitors(competitionId)
    }

    val competition by viewModel.selectedCompetition.collectAsState()
    val courses = viewModel.courses.collectAsState().value
    val competitors = viewModel.competitors.collectAsState().value
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text( "Gestion de la compétition " + competition?.name ) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
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

            viewModel.loadCoursesForCompetition(competitionId)

            if (courses.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).height(300.dp)) {
                    items(courses) { course ->
                        CourseItem(
                            course = course,
                            onEdit = {
                                navController.navigate("updateCourse/${course.id}") },
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

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = { navController.navigate("updateCompetition/${competition!!.id}") }) {
                    Text("Modifier la competition")
                }
                Button(onClick =  { showDialog = true }  ) {
                    Text("Supprimer")
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmer la suppression") },
            text = { Text("Voulez-vous vraiment supprimer cette compétition ?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteCompetition(competition!!.id); navController.popBackStack()
                    showDialog = false
                }) {
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
