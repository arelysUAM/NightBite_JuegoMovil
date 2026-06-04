package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.ui.model.NightLevel

/**
 * Botón visual para representar una noche/nivel dentro del HomeScreen.
 *
 * Se mantiene compacto para poder mostrar varias noches en fila.
 */
@Composable
fun NightLevelButton(
    level: NightLevel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = level.isUnlocked,
        modifier = modifier.width(150.dp),
        contentPadding = PaddingValues(
            horizontal = 8.dp,
            vertical = 12.dp
        ),
        colors = ButtonDefaults.buttonColors()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = level.title,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )

            Text(
                text = level.subtitle,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}