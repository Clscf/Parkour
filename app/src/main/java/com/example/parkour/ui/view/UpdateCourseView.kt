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
import com.example.parkour.ui.items.ObstacleItem

@Composable
fun UpdateCourseView(courseId: Int, courseViewModel: CourseViewModel, obstacleViewModel: ObstacleViewModel, navController: NavController) {
    val courses = courseViewModel.courses.collectAsState().value
    val course = courses.find { it.id == courseId }
    val obstacles = obstacleViewModel.obstacles.collectAsState().value
    val obstacleassocie = courseViewModel.courseObstacles.collectAsState().value

    var courseName by remember { mutableStateOf("") }
    var maxDuration by remember { mutableStateOf("") }

    LaunchedEffect(course) {
        if (course != null) {
            courseName = course.name
            maxDuration = course.maxDuration.toString()
        }
    }

    LaunchedEffect(courseId) {
        courseViewModel.loadCourses()
        courseViewModel.loadObstaclesForCourse(courseId)
        obstacleViewModel.loadAllObstacles()
    }

    if (course != null) {
        Scaffold(
            bottomBar = {
                BottomAppBar {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { navController.popBackStack() }) {
                            Text("Annuler")
                        }

                        Button(
                            onClick = {
                                val updatedCourse = CourseUpdate(
                                    name = courseName,
                                    maxDuration = maxDuration.toIntOrNull() ?: course.maxDuration,
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
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text("Modifier la course", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = courseName,
                    onValueChange = { courseName = it },
                    label = { Text("Nom de la course") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = maxDuration,
                    onValueChange = { maxDuration = it.filter { char -> char.isDigit() } },
                    label = { Text("Durée maximale (minutes)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(26.dp))

                Text("Obstacles Associés :", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))

                if (obstacleassocie.isEmpty()) {
                    Text(
                        text = "Aucun obstacle associé pour le moment.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                    ) {
                        items(obstacleassocie) { obstacle ->
                            CourseObstacleItem(
                                obstacle = obstacle,
                                onDelete = {
                                    val obstacleId = obstacles.find { it.name == obstacle.obstacleName }?.id
                                    if (obstacleId != null) {
                                        courseViewModel.removeObstacleFromCourse(courseId, obstacleId)
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Obstacles Disponibles :", style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = { navController.navigate("create_obstacle_view/$courseId") }) {
                        Text("+")
                    }
                }

                val obstaclesDisponibles = obstacles.filter { obstacleDispo ->
                    obstacleassocie.none { it.obstacleName == obstacleDispo.name }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                ) {
                    items(obstaclesDisponibles) { obstacle ->
                        ObstacleItem(
                            obstacle = obstacle,
                            onAdd = { courseViewModel.addObstacleToCourse(courseId, obstacle) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    } else {
        Text("Course introuvable", style = MaterialTheme.typography.bodyLarge)
    }
}
