package com.example.parkour.ui.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.data.model.Competitor
import com.example.parkour.ui.items.CompetitorItem
import com.example.parkour.ui.viewmodel.CompetitionViewModel
import com.example.parkour.ui.viewmodel.CompetitorViewModel
import java.time.LocalDate
import java.time.Period

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompetitorView(
    viewModelCompetition: CompetitionViewModel,
    viewModelCompetitor: CompetitorViewModel,
    navController: NavController,
    competitionId: Int
) {
    val competitors = viewModelCompetitor.competitors.collectAsState().value
    val competition = viewModelCompetition.competitions.collectAsState().value
        .find { it.id == competitionId }
    val alreadyAddedCompetitors by  viewModelCompetition.competitors.collectAsState()


    val alreadyAddedIds = alreadyAddedCompetitors.map { it.id }.toSet()

    // Filtrer les compétiteurs en fonction de l'âge, du genre et de la présence dans la compétition
    val filteredCompetitors = competitors.filter { competitor ->
        val birthDate = runCatching { LocalDate.parse(competitor.bornAt) }.getOrNull()
        val isAgeValid = birthDate?.let {
            val age = Period.between(it, LocalDate.now()).years
            age in (competition?.ageMin ?: 0)..(competition?.ageMax ?: Int.MAX_VALUE)
        } ?: false

        val isGenderValid = competition?.gender == null || competitor.gender == competition.gender
        val isAlreadyAdded = competitor.id in alreadyAddedIds

        isAgeValid && isGenderValid && !isAlreadyAdded // Ne pas afficher ceux qui sont déjà ajoutés
    }

    LaunchedEffect(alreadyAddedCompetitors) {
        Log.d("CompetitorView", "Compétiteurs déjà ajoutés: $alreadyAddedCompetitors")
    }

    var selectedCompetitors by remember { mutableStateOf(setOf<Competitor>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajouter des compétiteurs") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    selectedCompetitors.forEach { competitor ->
                        viewModelCompetition.addCompetitorToCompetition(competitionId, competitor)
                    }
                    selectedCompetitors = emptySet()
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = selectedCompetitors.isNotEmpty()
            ) {
                Text("Ajouter ${selectedCompetitors.size} compétiteur(s)")
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
            if (filteredCompetitors.isEmpty()) {
                Text("Aucun compétiteur disponible pour cette compétition.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredCompetitors) { competitor ->
                        val isSelected = selectedCompetitors.contains(competitor)
                        CompetitorItem(
                            competitor = competitor,
                            isSelected = isSelected,
                            onSelectionChange = { isChecked ->
                                selectedCompetitors = if (isChecked) {
                                    selectedCompetitors + competitor
                                } else {
                                    selectedCompetitors - competitor
                                }
                            },
                            onDelete = {}, // Pas besoin de suppression ici
                            isAlreadyAdded = false
                        )
                    }
                }
            }
        }
    }
}



