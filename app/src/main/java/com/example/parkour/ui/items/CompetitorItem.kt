package com.example.parkour.ui.items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.parkour.data.model.Competitor

@Composable
fun CompetitorItem(
    competitor: Competitor,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    isAlreadyAdded: Boolean
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = if (isAlreadyAdded) Color.LightGray else Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "${competitor.lastName} ${competitor.firstName}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Genre: ${competitor.gender}  Tel: ${competitor.phone}", style = MaterialTheme.typography.bodyMedium)
            }

            // Checkbox pour sélectionner uniquement les nouveaux compétiteurs
            if (!isAlreadyAdded) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = onSelectionChange
                )
            }

            // Supprimer uniquement les compétiteurs non ajoutés
            if (!isAlreadyAdded) {
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer", tint = Color.Red)
                }
            }
        }
    }
}
