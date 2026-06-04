package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow

/**
 * Card informativa del enemigo de cada noche.
 *
 * Se usa en LevelIntroScreen antes de iniciar el nivel.
 */
@Composable
fun EnemyCard(
    enemyName: String,
    enemyDescription: String,
    enemyBehavior: String,
    survivalTip: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp))
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Advertencia de enemigo",
                tint = CheeseYellow
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = enemyName,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Descripción",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = enemyDescription,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Comportamiento",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = enemyBehavior,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Consejo de supervivencia",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = survivalTip,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}