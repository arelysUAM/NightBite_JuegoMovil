package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
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
    NightBaseCard(
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Advertencia de enemigo",
            tint = CheeseYellow
        )

        Spacer(modifier = Modifier.height(NightSpacing.small))

        Text(
            text = enemyName,
            style = MaterialTheme.typography.titleLarge
        )

        EnemyInfoSection(
            title = "Descripción",
            content = enemyDescription
        )

        EnemyInfoSection(
            title = "Comportamiento",
            content = enemyBehavior
        )

        EnemyInfoSection(
            title = "Consejo de supervivencia",
            content = survivalTip
        )
    }
}

@Composable
private fun EnemyInfoSection(
    title: String,
    content: String
) {
    Spacer(modifier = Modifier.height(NightSpacing.medium))

    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(modifier = Modifier.height(NightSpacing.extraSmall))

    Text(
        text = content,
        style = MaterialTheme.typography.bodyMedium
    )
}