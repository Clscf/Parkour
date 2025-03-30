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
    competitionId: Int // Ajouter competitionId pour gérer l'ajout et la suppression
) {
    // Liste des compétiteurs
    val competitors = viewModelCompetitor.competitors.collectAsState().value

    // Récupérer les critères de la compétition
    val competition = viewModelCompetition.competitions.collectAsState().value
        .find { it.id == competitionId }

    // Filtrer les compétiteurs en fonction de l'âge (calculé à partir de la date de naissance) et du genre
    val filteredCompetitors = competitors.filter { competitor ->
        // Convertir la chaîne de date de naissance en LocalDate
        val birthDate = try {
            LocalDate.parse(competitor.bornAt)  // Supposons que bornAt soit au format "yyyy-MM-dd"
        } catch (e: Exception) {
            null  // Si la date est invalide, on ignore ce compétiteur
        }

        // Si la date de naissance est valide, on peut procéder au calcul de l'âge
        val isAgeValid = birthDate?.let {
            val currentDate = LocalDate.now()
            val age = Period.between(it, currentDate).years
            age >= (competition?.ageMin ?: 0) && age <= (competition?.ageMax ?: Int.MAX_VALUE)
        } ?: false  // Si la date est invalide, on considère que l'âge n'est pas valide
        
        val isGenderValid = competition?.gender == null || competitor.gender == competition.gender

        isAgeValid && isGenderValid
    }

    var selectedCompetitors by remember { mutableStateOf<MutableSet<Competitor>>(mutableSetOf()) }

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
                                if (isSelected) {
                                    selectedCompetitors.add(competitor)
                                } else {
                                    selectedCompetitors.remove(competitor)
                                }
                            },
                            onDelete = {
                                viewModelCompetition.removeCompetitorFromCompetition(competitor.id, competitionId)
                            }
                        )
                    }
                }
            }
        }
    }
}


