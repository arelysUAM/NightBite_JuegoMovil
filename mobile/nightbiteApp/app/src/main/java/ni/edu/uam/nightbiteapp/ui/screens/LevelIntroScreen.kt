package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.ui.components.EnemyCard
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.model.NightLevel
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow

/**
 * Pantalla previa antes de iniciar una noche.
 *
 * Muestra información narrativa del nivel y la card del enemigo.
 */
@Composable
fun LevelIntroScreen(
    level: NightLevel?,
    onStartLevel: () -> Unit,
    onBackToHome: () -> Unit
) {
    if (level == null) {
        NightMessageDialog(
            title = "Nivel no encontrado",
            message = "No se pudo cargar la información de esta noche.",
            confirmText = "Volver",
            dismissText = null,
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onBackToHome,
            onDismiss = onBackToHome
        )

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = level.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = level.subtitle,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(PaddingValues(16.dp))
            ) {
                Text(
                    text = "Mensaje recibido",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = level.narrativeMessage,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        EnemyCard(
            enemyName = level.enemyName,
            enemyDescription = level.enemyDescription,
            enemyBehavior = level.enemyBehavior,
            survivalTip = level.survivalTip
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onStartLevel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Iniciar")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver")
        }
    }
}