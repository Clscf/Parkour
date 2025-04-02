package com.example.parkour.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.ui.items.CardTitleItem
import com.example.parkour.ui.viewmodel.CompetitionViewModel
import com.example.parkour.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCompetitionView(
    viewModel: CompetitionViewModel,
    courseViewModel: CourseViewModel,
    competitionId: Int,
    navController: NavController
) {
    val competition by viewModel.selectedCompetition.collectAsState()
    val courses by viewModel.courses.collectAsState()
    val obstacles by viewModel.obstacles.collectAsState()
    val competitors by viewModel.competitors.collectAsState()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(competitionId) {
        viewModel.getCompetitionById(competitionId)
        viewModel.loadCoursesForCompetition(competitionId)
        viewModel.loadCompetitionCompetitors(competitionId)
        isLoading = false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(competition?.name ?: "Détails de la compétition") },
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
                    onClick = { navController.navigate("addCompetitorCompetition/${competitionId}") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text("Ajouter un compétiteur")
                }
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            competition?.let {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CardTitleItem("Informations générales") {
                        Text("Âge: ${it.ageMin} - ${it.ageMax}", style = MaterialTheme.typography.bodyMedium)
                        Text("Statut: ${it.status}", style = MaterialTheme.typography.bodyMedium)
                    }

                    CardTitleItem("Courses") {
                        if (courses.isEmpty()) {
                            Text("Aucune course disponible.", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            var expandedCourseId by remember { mutableStateOf<Int?>(null) }
                            val courseObstacles by courseViewModel.courseObstacles.collectAsState()

                            LazyColumn(Modifier.height(200.dp)) {
                                items(courses) { course ->
                                    Column {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("- ${course.name}", modifier = Modifier.weight(1f))
                                            IconButton(onClick = {
                                                if (expandedCourseId == course.id) {
                                                    expandedCourseId = null
                                                } else {
                                                    expandedCourseId = course.id
                                                    courseViewModel.loadObstaclesForCourse(course.id) // 🔥 Charge les obstacles
                                                }
                                            }) {
                                                Icon(
                                                    imageVector = if (expandedCourseId == course.id)
                                                        Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                                                    contentDescription = "Voir obstacles"
                                                )
                                            }
                                        }

                                        if (expandedCourseId == course.id) {
                                            if (courseObstacles.isNotEmpty()) {
                                                Column(modifier = Modifier.padding(start = 16.dp)) {
                                                    courseObstacles.forEach { obstacle ->
                                                        Text("- ${obstacle.obstacleName}", style = MaterialTheme.typography.bodyMedium)
                                                    }
                                                }
                                            } else {
                                                Text("Aucun obstacle associé.", modifier = Modifier.padding(start = 16.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    CardTitleItem("Compétiteurs") {
                        if (competitors.isEmpty()) {
                            Text("Aucun compétiteur inscrit.", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            LazyColumn(Modifier.height(140.dp)) {
                                items(competitors) { Text("- ${it.firstName} ${it.lastName}") }
                            }
                        }
                    }
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Erreur : Impossible de charger les détails.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
