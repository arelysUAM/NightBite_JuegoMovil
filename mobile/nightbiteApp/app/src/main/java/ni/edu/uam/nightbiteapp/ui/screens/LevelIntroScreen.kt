package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ni.edu.uam.nightbiteapp.ui.components.EnemyCard
import ni.edu.uam.nightbiteapp.ui.components.NightBaseCard
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.NightPrimaryButton
import ni.edu.uam.nightbiteapp.ui.components.NightSecondaryButton
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.model.NightLevel
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import ni.edu.uam.nightbiteapp.ui.design.getNightWindowSize
import ni.edu.uam.nightbiteapp.ui.design.nightDimensionsFor

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
        LevelNotFoundDialog(
            onBackToHome = onBackToHome
        )
        return
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val windowSize = getNightWindowSize(maxWidth)
        val dimensions = nightDimensionsFor(windowSize)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = dimensions.screenHorizontalPadding,
                    vertical = dimensions.screenVerticalPadding
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.widthIn(
                    max = dimensions.cardMaxWidth
                )
            ) {
                LevelIntroHeader(
                    title = level.title,
                    subtitle = level.subtitle
                )

                Spacer(modifier = Modifier.height(dimensions.sectionSpacing))

                NarrativeMessageCard(
                    message = level.narrativeMessage
                )

                Spacer(modifier = Modifier.height(dimensions.sectionSpacing))

                EnemyCard(
                    enemyName = level.enemyName,
                    enemyDescription = level.enemyDescription,
                    enemyBehavior = level.enemyBehavior,
                    survivalTip = level.survivalTip
                )

                Spacer(modifier = Modifier.height(dimensions.sectionSpacing))

                LevelIntroActions(
                    itemSpacing = dimensions.itemSpacing,
                    onStartLevel = onStartLevel,
                    onBackToHome = onBackToHome
                )
            }
        }
    }
}

@Composable
private fun LevelIntroActions(
    itemSpacing: Dp,
    onStartLevel: () -> Unit,
    onBackToHome: () -> Unit
) {
    NightPrimaryButton(
        text = "Iniciar",
        onClick = onStartLevel,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(itemSpacing))

    NightSecondaryButton(
        text = "Volver",
        onClick = onBackToHome,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun LevelIntroHeader(
    title: String,
    subtitle: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium
    )

    Spacer(modifier = Modifier.height(NightSpacing.extraSmall))

    Text(
        text = subtitle,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun NarrativeMessageCard(
    message: String
) {
    NightBaseCard {
        Text(
            text = "Mensaje recibido",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(NightSpacing.small))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun LevelNotFoundDialog(
    onBackToHome: () -> Unit
) {
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
}