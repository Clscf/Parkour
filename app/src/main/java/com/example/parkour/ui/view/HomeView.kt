package com.example.parkour.ui.view

import CompetitionItem
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.ui.viewmodel.CompetitionViewModel
import com.example.parkour.data.model.Competition

@Composable
fun HomeView(viewModel: CompetitionViewModel, navController: NavController) {
    val competitions = viewModel.competitions.collectAsState().value
    var isEditorMode by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { SimpleBottomNavigation(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Compétitions disponibles",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Checkbox(
                    checked = isEditorMode,
                    onCheckedChange = { isEditorMode = it }
                )
                Text("Mode Éditeur")
            }

            if (competitions.isEmpty()) {
                Text("Aucune compétition disponible.", modifier = Modifier.padding(bottom = 16.dp))
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(competitions) { competition ->
                        CompetitionItem(competition, navController, isEditorMode)
                    }
                }
            }
        }
    }
}
