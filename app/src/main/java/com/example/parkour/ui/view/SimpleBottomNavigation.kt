package com.example.parkour.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.collectAsState
import com.example.parkour.ui.viewmodel.CompetitionViewModel

@Composable
fun SimpleBottomNavigation(navController: NavController, viewModel: CompetitionViewModel) {
    val isEditing by viewModel.isEditing.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icône Accueil
        IconButton(onClick = { navController.navigate("home") }) {
            Column(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Accueil",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Accueil",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1
                )
            }
        }

        // Icône Créer (désactivé en mode éditeur)
        IconButton(onClick = { if (!isEditing) navController.navigate("createCompetition") }, enabled = !isEditing) {
            Column(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Créer",
                    tint = if (isEditing) Color.Gray else Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Créer",
                    color = if (isEditing) Color.Gray else Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1
                )
            }
        }

        // Icône Arbitrage
        IconButton(onClick = { navController.navigate("arbitration/1/1") }) {
            Column(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Arbitrage",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Arbitrage",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1
                )
            }
        }
    }
}
