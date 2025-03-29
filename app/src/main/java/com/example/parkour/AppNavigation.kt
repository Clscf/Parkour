package com.example.parkour.ui.screen

import HomeView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parkour.data.repository.CompetitionRepository
import com.example.parkour.network.RetrofitInstance
import com.example.parkour.ui.view.CreateCompetitionView
import com.example.parkour.ui.viewmodel.CompetitionViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val apiService = remember { RetrofitInstance.apiService }
    val competitionViewModel = remember { CompetitionViewModel(CompetitionRepository(apiService)) }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeView(competitionViewModel, navController)
        }
        composable("createCompetition") {
            CreateCompetitionView(competitionViewModel, navController)
        }
    }
}

