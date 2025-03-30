package com.example.parkour.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.data.model.uptdate.CourseUpdate
import com.example.parkour.viewmodel.CourseViewModel
import com.example.parkour.viewmodel.ObstacleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCourseView(courseId: Int, courseViewModel: CourseViewModel, obstacleViewModel: ObstacleViewModel, navController: NavController) {
    val courses = courseViewModel.courses.collectAsState().value
    val course = courses.find { it.id == courseId }
    val obstacles = obstacleViewModel.obstacles.collectAsState().value

    // Charger les obstacles de la course
    LaunchedEffect(courseId) {
        courseViewModel.loadObstaclesForCourse(courseId)
        obstacleViewModel.loadAllObstacles() // Charger tous les obstacles existants
    }

    // Si la course est trouvée, afficher l'interface
    if (course != null) {
        Scaffold(
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text("Modifier la course", style = MaterialTheme.typography.headlineSmall)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nom de la course
                    OutlinedTextField(
                        value = course.name,
                        onValueChange = { /* Mettre à jour le nom si besoin */ },
                        label = { Text("Nom de la course") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Durée maximale de la course
                    OutlinedTextField(
                        value = course.maxDuration.toString(),
                        onValueChange = { /* Mettre à jour la durée si besoin */ },
                        label = { Text("Durée maximale (minutes)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Affichage des obstacles associés à la course
                    Text("Obstacles associés :", style = MaterialTheme.typography.bodyLarge)

                    // Limiter la taille de la LazyColumn pour éviter qu'elle prenne trop de place
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp) // Limite de hauteur
                    ) {
                        items(obstacles) { obstacle ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = obstacle.name, style = MaterialTheme.typography.bodyMedium)
                                Button(
                                    onClick = {
                                        courseViewModel.addObstacleToCourse(courseId, obstacle.id)
                                    },
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                ) {
                                    Text("Ajouter")
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bouton pour naviguer vers la page de création d'obstacle
                    Button(
                        onClick = {
                            navController.navigate("create_obstacle_view/$courseId")
                        }
                    ) {
                        Text("Ajouter un nouvel obstacle")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Boutons de sauvegarde et annulation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { navController.popBackStack() }) {
                            Text("Annuler")
                        }

                        Button(
                            onClick = {
                                // Sauvegarder les changements de la course
                                val updatedCourse = CourseUpdate(
                                    name = course.name,
                                    maxDuration = course.maxDuration,
                                    position = course.position,
                                    isOver = course.isOver,
                                    competitionId = course.competitionId
                                )
                                courseViewModel.updateCourse(courseId, updatedCourse)
                                navController.popBackStack()
                            }
                        ) {
                            Text("Sauvegarder")
                        }
                    }
                }
            }
        )
    } else {
        // Si la course n'est pas trouvée
        Text("Course introuvable", style = MaterialTheme.typography.bodyLarge)
    }
}


