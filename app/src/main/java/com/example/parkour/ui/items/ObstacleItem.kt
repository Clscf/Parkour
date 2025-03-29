import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parkour.data.model.Obstacle

@Composable
fun ObstacleItem(obstacle: Obstacle, onObstacleSelected: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = obstacle.isSelected,
            onCheckedChange = { isChecked ->
                onObstacleSelected(isChecked)
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = obstacle.obstacleName)
    }
}