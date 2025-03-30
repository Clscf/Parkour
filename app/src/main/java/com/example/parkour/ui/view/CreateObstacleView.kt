package com.example.parkour.ui.screens

import android.util.Log
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
import com.example.parkour.data.model.create.ObstacleCreate
import com.example.parkour.viewmodel.CourseViewModel
import com.example.parkour.viewmodel.ObstacleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateObstacleView(courseId: Int, viewModel: CourseViewModel, viewModel2: ObstacleViewModel, navController: NavController) {
    val obstacles = viewModel.courseObstacles.collectAsState().value
    var newObstacleName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") } // Variable pour afficher les erreurs

    LaunchedEffect(courseId) {
        //viewModel.loadObstaclesForCourse(courseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Créer un Obstacle") })
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text("Créer un nouveau obstacle", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))

                // Affichage des erreurs
                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Créer un nouveau obstacle
                OutlinedTextField(
                    value = newObstacleName,
                    onValueChange = { newObstacleName = it },
                    label = { Text("Nom de l'obstacle") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (newObstacleName.isNotEmpty()) {
                            // Créer un obstacle dans l'API
                            viewModel2.createObstacle(ObstacleCreate(name = newObstacleName),
                                onSuccess = {
                                    // Callback après la création de l'obstacle
                                    navController.popBackStack() // Revenir à la page précédente
                                },
                                onFailure = { error ->
                                    // Gérer l'erreur et l'afficher
                                    errorMessage = error
                                })
                        } else {
                            errorMessage = "Le nom de l'obstacle ne peut pas être vide"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Créer un obstacle")
                }
            }
        }
    )
}


