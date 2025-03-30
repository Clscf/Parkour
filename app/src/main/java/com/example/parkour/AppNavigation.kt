package com.example.parkour.ui.screen

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.parkour.HomeView
import com.example.parkour.data.model.uptdate.CompetitionUpdate
import com.example.parkour.data.repository.ArbitrationRepository
import com.example.parkour.network.RetrofitInstance
import com.example.parkour.repository.CompetitionRepository
import com.example.parkour.ui.view.ArbitrationView
import com.example.parkour.ui.view.CreateCompetitionView
import com.example.parkour.ui.view.UpdateCompetitionView
import com.example.parkour.ui.viewmodel.ArbitrationViewModel
import com.example.parkour.ui.viewmodel.CompetitionViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val apiService = remember { RetrofitInstance.apiService }

    val competitionViewModel = remember { CompetitionViewModel(CompetitionRepository(apiService)) }
    val arbitrationViewModel = remember { ArbitrationViewModel(ArbitrationRepository(apiService)) }

    NavHost(navController = navController, startDestination = "home") {
        // Page d'accueil
        composable("home") {
            HomeView(competitionViewModel, navController)
        }

        // Page de création de compétition
        composable("createCompetition") {
            CreateCompetitionView(competitionViewModel, navController)
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
                        hasRetry = it.hasTry,
                        status = it.status
                    ),
                    competitionId = it.id
                )
            }
        }

        // Page d'arbitrage
        composable(
            "arbitration/{competitionId}/{courseId}",
            arguments = listOf(
                navArgument("competitionId") { type = NavType.IntType },
                navArgument("courseId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val competitionId = backStackEntry.arguments?.getInt("competitionId") ?: return@composable
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: return@composable

            ArbitrationView(
                viewModel = arbitrationViewModel,
                navController = navController,
                competitionId = competitionId,
                courseId = courseId
            )
        }
    }
}