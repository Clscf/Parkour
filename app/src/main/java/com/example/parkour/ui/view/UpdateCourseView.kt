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
import com.example.parkour.data.model.Obstacle
import com.example.parkour.data.model.uptdate.CourseUpdate
import com.example.parkour.ui.items.CourseObstacleItem
import com.example.parkour.viewmodel.CourseViewModel
import com.example.parkour.viewmodel.ObstacleViewModel
import com.example.parkour.ui.items.ObstacleItem  // Importer l'ObstacleItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCourseView(courseId: Int, courseViewModel: CourseViewModel, obstacleViewModel: ObstacleViewModel, navController: NavController) {
    val courses = courseViewModel.courses.collectAsState().value
    val course = courses.find { it.id == courseId }
    val obstacles = obstacleViewModel.obstacles.collectAsState().value
    val obstacleassocie = courseViewModel.courseObstacles.collectAsState().value

    // Définir les variables d'état pour le nom et la durée de la course
    var courseName by remember { mutableStateOf(course?.name ?: "") }
    var maxDuration by remember { mutableStateOf(course?.maxDuration?.toString() ?: "") }

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
                        value = courseName,  // Utilisez courseName pour lier l'état
                        onValueChange = { courseName = it },  // Met à jour courseName lorsqu'une modification est faite
                        label = { Text("Nom de la course") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Durée maximale de la course
                    OutlinedTextField(
                        value = maxDuration,  // Utilisez maxDuration pour lier l'état
                        onValueChange = { maxDuration = it },  // Met à jour maxDuration lorsqu'une modification est faite
                        label = { Text("Durée maximale (minutes)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Obstacles Associés :", style = MaterialTheme.typography.bodyLarge)

                    // Limiter la taille de la LazyColumn pour éviter qu'elle prenne trop de place
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp) // Limite de hauteur
                    ) {
                        items(obstacleassocie) { obstacle ->
                            CourseObstacleItem(
                                obstacle = obstacle,
                                onDelete = {
                                    val obstacleId = obstacles.find { it.name == obstacle.obstacleName }?.id
                                    if (obstacleId != null) {
                                        courseViewModel.removeObstacleFromCourse(
                                            courseId,
                                            obstacleId
                                        )
                                    }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Affichage des obstacles disponibles
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Obstacles Disponibles :", style = MaterialTheme.typography.bodyLarge)
                        Button(onClick = { navController.navigate("create_obstacle_view/$courseId") }) {
                            Text("+")
                        }
                    }

                    val obstaclesDisponibles = obstacles.filter { obstacleDispo ->
                        obstacleassocie.none { it.obstacleName == obstacleDispo.name }
                    }


                    // Limiter la taille de la LazyColumn pour éviter qu'elle prenne trop de place
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                    ) {
                        items(obstaclesDisponibles) { obstacle ->
                            ObstacleItem(
                                obstacle = obstacle,
                                onAdd = {
                                    courseViewModel.addObstacleToCourse(courseId, obstacle)
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
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
                                    name = courseName,  // Utilisez courseName ici
                                    maxDuration = maxDuration.toIntOrNull() ?: course.maxDuration,  // Convertissez maxDuration en Int
                                    position = course.position,
                                    isOver = course.isOver,
                                    competitionId = course.competitionId
                                )
                                courseViewModel.updateCourse(courseId, updatedCourse)
                                navController.popBackStack()
                            }
                        ) {
                            Text("Retour")
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
