import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.parkour.data.model.Competition

@Composable
fun CompetitionItem(competition: Competition, navController: NavController, isEditing: Boolean) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { isExpanded = !isExpanded },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = competition.name, style = MaterialTheme.typography.headlineSmall)
            Text(text = "Âge: ${competition.ageMin} - ${competition.ageMax}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Statut: ${competition.status}", style = MaterialTheme.typography.bodyMedium)

            if (isExpanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (isEditing){
                        Button(onClick = { navController.navigate("competitionEditor/${competition.id}") }) {
                            Text("Gérer")
                        }
                    }
                    else{
                        Button(onClick = { navController.navigate("competitionEditor/${competition.id}") }, enabled = false) {
                            Text("Gérer")
                        }
                    }
                    Button(onClick = { navController.navigate("competitionDetails/${competition.id}") }) {
                        Text("Détails")
                    }
                }
            }
        }
    }
}
