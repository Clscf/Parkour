package com.example.parkour.ui.view

import android.os.Build
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
    val alreadyAddedCompetitors = viewModelCompetition.getCompetitorsForCompetition(competitionId)
        .collectAsState(initial = emptyList()).value

    val filteredCompetitors = competitors.filter { competitor ->
        val birthDate = try {
            LocalDate.parse(competitor.bornAt)
        } catch (e: Exception) {
            null
        }

        val isAgeValid = birthDate?.let {
            val currentDate = LocalDate.now()
            val age = Period.between(it, currentDate).years
            age in (competition?.ageMin ?: 0)..(competition?.ageMax ?: Int.MAX_VALUE)
        } ?: false

        val isGenderValid = competition?.gender == null || competitor.gender == competition.gender
        val isAlreadyAdded = alreadyAddedCompetitors.any { it.id == competitor.id }

        isAgeValid && isGenderValid && !isAlreadyAdded
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
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                enabled = selectedCompetitors.isNotEmpty()
            ) {
                Text("Ajouter aux compétitions")
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
                        CompetitorItem(
                            competitor = competitor,
                            isSelected = selectedCompetitors.contains(competitor),
                            onSelectionChange = { isSelected ->
                                selectedCompetitors = if (isSelected) {
                                    selectedCompetitors + competitor
                                } else {
                                    selectedCompetitors - competitor
                                }
                            },
                            onDelete = {},
                            isAlreadyAdded = false
                        )
                    }
                }
            }
        }
    }
}
