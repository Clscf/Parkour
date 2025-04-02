package com.example.parkour.ui.view

import CompetitionItem
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.ui.viewmodel.CompetitionViewModel
import com.example.parkour.data.model.Competition

@Composable
fun HomeView(viewModel: CompetitionViewModel, navController: NavController) {
    val competitions = viewModel.competitions.collectAsState().value
    val isEditing by viewModel.isEditMode.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { SimpleBottomNavigation(navController = navController, isEditing) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bouton toggle mode éditeur
            EditorModeToggle(isEditing) {
                viewModel.toggleEditMode() // Appelle la fonction du ViewModel
            }

            Text(
                text = "Compétitions disponibles",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (competitions.isEmpty()) {
                Text("Aucune compétition disponible.", modifier = Modifier.padding(bottom = 16.dp))
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(competitions) { competition ->
                        CompetitionItem(competition, navController, isEditing)
                    }
                }
            }
        }
    }
}

@Composable
fun EditorModeToggle(isEditing: Boolean, onToggle: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onToggle() } // Inverse l'état via ViewModel
            .padding(8.dp)
    ) {
        Text(
            text = if (isEditing) "Mode Édition: ON" else "Mode Édition: OFF",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .width(50.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(if (isEditing) MaterialTheme.colorScheme.primary else Color.Gray)
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .offset(x = if (isEditing) 20.dp else 0.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .animateContentSize()
            )
        }
    }
}

