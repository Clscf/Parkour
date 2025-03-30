package com.example.parkour.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.data.model.Course
import com.example.parkour.data.model.uptdate.CourseUpdate
import com.example.parkour.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCourseView(courseId: Int, viewModel: CourseViewModel, navController: NavController) {
    val courses = viewModel.courses.collectAsState().value
    val course = courses.find { it.id == courseId }
    Log.d("UpdateCourseView", "Course ID reçu: $courseId")
    Log.d("UpdateCourseView", "Liste des courses: ${courses.map { it.id }}")

    var courseName by remember { mutableStateOf(course?.name ?: "") }
    var maxDuration by remember { mutableStateOf(course?.maxDuration?.toString() ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Modifier la Course") })
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                if (course == null) {
                    Text("Course introuvable", style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Retour")
                    }
                } else {
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
                        onValueChange = { maxDuration = it },
                        label = { Text("Durée maximale (minutes)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                                viewModel.updateCourse(courseId, updatedCourse)
                                navController.popBackStack()
                            }
                        ) {
                            Text("Sauvegarder")
                        }
                    }
                }
            }
        }
    )
}
