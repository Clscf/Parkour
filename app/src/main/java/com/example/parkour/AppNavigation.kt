package com.example.parkour.ui.screen

import DetailCompetitionView
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.parkour.ui.view.HomeView
import com.example.parkour.data.model.uptdate.CompetitionUpdate
import com.example.parkour.data.repository.ArbitrationRepository
import com.example.parkour.network.RetrofitInstance
import com.example.parkour.repository.CompetitionRepository
import com.example.parkour.repository.CompetitorRepository
import com.example.parkour.ui.view.ArbitrationView
import com.example.parkour.repository.CourseRepository
import com.example.parkour.repository.ObstacleRepository
import com.example.parkour.ui.screens.CreateObstacleView
import com.example.parkour.ui.screens.UpdateCourseView
import com.example.parkour.ui.view.CompetitorView
import com.example.parkour.ui.view.CreateCompetitionView
import com.example.parkour.ui.view.CreateCourseView
import com.example.parkour.ui.view.UpdateCompetitionView
import com.example.parkour.viewmodel.ArbitrationViewModel
import com.example.parkour.ui.viewmodel.CompetitionViewModel
import com.example.parkour.ui.viewmodel.CompetitorViewModel
import com.example.parkour.viewmodel.CourseViewModel
import com.example.parkour.ui.screens.CreateObstacleView  // Ajout de l'import de CreateObstacleView
import com.example.parkour.ui.view.CompetitionEditorView
import com.example.parkour.viewmodel.ObstacleViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val apiService = remember { RetrofitInstance.apiService }

    val competitionViewModel = remember { CompetitionViewModel(CompetitionRepository(apiService)) }
    val arbitrationViewModel = remember { ArbitrationViewModel(ArbitrationRepository(apiService)) }
    val courseViewModel = remember { CourseViewModel(CourseRepository(apiService)) }
    val competitorViewModel = remember { CompetitorViewModel(CompetitorRepository(apiService))  }
    val obstacleViewModel = remember { ObstacleViewModel(ObstacleRepository(apiService)) }

    NavHost(navController = navController, startDestination = "home") {
        // Page d'accueil
        composable("home") {
            HomeView(competitionViewModel,navController)
        }

        // Page de création de compétition
        composable("createCompetition") {
            CreateCompetitionView(competitionViewModel, navController)
        }

        composable("addCompetitorCompetition/{competitionId}", arguments = listOf(
            navArgument("competitionId") { type = NavType.IntType }
        )) { backStackEntry ->
            val competitionId = backStackEntry.arguments?.getInt("competitionId") ?: return@composable
            CompetitorView(viewModelCompetition = competitionViewModel, viewModelCompetitor = competitorViewModel, navController = navController, competitionId = competitionId)
        }


        // Page de mise à jour de compétition
        composable(
            "updateCompetition/{competitionId}",
            arguments = listOf(navArgument("competitionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val competitionId = backStackEntry.arguments?.getInt("competitionId") ?: return@composable
            val competition = competitionViewModel.competitions.value.find { it.id == competitionId }

            competition?.let {
                UpdateCompetitionView(
                    viewModel = competitionViewModel,
                    navController = navController,
                    competition = CompetitionUpdate(
                        name = it.name,
                        ageMin = it.ageMin,
                        ageMax = it.ageMax,
                        gender = it.gender,
                        hasRetry = it.hasRetry,
                        status = it.status
                    ),
                    competitionId = it.id
                )
            }
        }

        // Page arbitrage
        composable(
            "arbitration/{competitionId}",
            arguments = listOf(
                navArgument("competitionId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val competitionId = backStackEntry.arguments?.getInt("competitionId") ?: return@composable

            ArbitrationView(
                viewModel = arbitrationViewModel,
                navController = navController,
                competitionId = competitionId // Passer uniquement competitionId
            )
        }


        composable("createCourse/{competitionId}") { backStackEntry ->
            val competitionId = backStackEntry.arguments?.getString("competitionId")?.toIntOrNull() ?: 0
            CreateCourseView(competitionViewModel, navController, competitionId)
        }
        composable(
            "updateCourse/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: return@composable
            UpdateCourseView(courseId = courseId, courseViewModel = courseViewModel, obstacleViewModel = obstacleViewModel, navController = navController)
        }

        // Page de création de l'obstacle
        composable(
            "create_obstacle_view/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: return@composable
            CreateObstacleView(courseId = courseId, viewModel = courseViewModel, viewModel2 = obstacleViewModel, navController = navController)
        }

        // Page d'édition de la compétition
        composable(
            "competitionEditor/{competitionId}",
            arguments = listOf(navArgument("competitionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val competitionId = backStackEntry.arguments?.getInt("competitionId") ?: return@composable
            CompetitionEditorView(competitionViewModel, courseViewModel,  navController, competitionId)
        }

        // Détails de la compétition
        composable(
            "competitionDetails/{competitionId}",
            arguments = listOf(navArgument("competitionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val competitionId = backStackEntry.arguments?.getInt("competitionId")
            Log.d("Navigation", "competitionId reçu: $competitionId")

            if (competitionId != null) {
                DetailCompetitionView(competitionViewModel, courseViewModel, competitionId, navController)
            } else {
                Log.e("Navigation", "Erreur: competitionId est null")
            }
        }


    }
}
