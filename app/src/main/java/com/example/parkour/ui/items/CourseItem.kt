package com.example.parkour.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.ui.draw.shadow
import com.example.parkour.data.model.Course

@Composable
fun CourseItem(course: Course) {
    // Simuler un "conteneur" avec un fond et une ombre en utilisant un Box
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White) // Simuler le fond blanc
            .shadow(4.dp) // Ajouter une ombre ici
            .padding(16.dp) // Ajouter du padding interne
    ) {
        Text(text = "Nom de la Course : ${course.name}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Durée Maximale : ${course.maxDuration}", style = MaterialTheme.typography.bodyMedium)
    }
}
