import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.ui.items.CardTitleItem
import com.example.parkour.ui.viewmodel.CompetitionViewModel
import com.example.parkour.ui.view.SimpleBottomNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCompetitionView(viewModel: CompetitionViewModel, competitionId: Int, navController: NavController) {
    val competition by viewModel.selectedCompetition.collectAsState()
    val courses by viewModel.courses.collectAsState()
    val obstacles by viewModel.obstacles.collectAsState()
    val competitors by viewModel.competitors.collectAsState()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(competitionId) {
        viewModel.getCompetitionById(competitionId)
        viewModel.loadCoursesForCompetition(competitionId)
        viewModel.loadCompetitionCompetitors(competitionId)
        isLoading = false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(competition?.name ?: "Détails de la compétition") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
        bottomBar = { SimpleBottomNavigation(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addCompetitorCompetition/${competitionId}") }) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter un compétiteur")
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            competition?.let {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CardTitleItem("Informations générales") {
                        Text("Âge: ${it.ageMin} - ${it.ageMax}", style = MaterialTheme.typography.bodyMedium)
                        Text("Statut: ${it.status}", style = MaterialTheme.typography.bodyMedium)
                    }

                    CardTitleItem("Courses") {
                        if (courses.isEmpty()) {
                            Text("Aucune course disponible.", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            LazyColumn { items(courses) { Text("- ${it.name}") } }
                        }
                    }

                    CardTitleItem("Obstacles") {
                        if (obstacles.isEmpty()) {
                            Text("Aucun obstacle enregistré.", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            LazyColumn { items(obstacles) { Text("- ${it.name}") } }
                        }
                    }

                    CardTitleItem("Compétiteurs") {
                        if (competitors.isEmpty()) {
                            Text("Aucun compétiteur inscrit.", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            LazyColumn { items(competitors) { Text("- ${it.firstName} ${it.lastName}") } }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = { navController.navigate("updateCompetition/${competition!!.id}") }) {
                            Text("Modifier")
                        }
                        Button(onClick = { viewModel.deleteCompetition(competition!!.id); navController.popBackStack() }) {
                            Text("Supprimer")
                        }
                        Button(onClick = { navController.navigate("arbitration/${competition!!.id}/1") }) {
                            Text("Arbitrer")
                        }
                    }
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Erreur : Impossible de charger les détails.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


