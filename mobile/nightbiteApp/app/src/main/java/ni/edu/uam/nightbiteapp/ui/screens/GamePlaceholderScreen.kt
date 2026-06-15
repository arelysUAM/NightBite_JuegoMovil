package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import ni.edu.uam.nightbiteapp.data.local.mock.NightLevelsData
import ni.edu.uam.nightbiteapp.ui.components.cards.NightBaseCard
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.buttons.NightPrimaryButton
import ni.edu.uam.nightbiteapp.ui.components.buttons.NightSecondaryButton
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.model.GameResultType
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import androidx.compose.ui.unit.dp

/**
 * Simulador temporal de una partida.
 *
 * Permite probar los distintos resultados y el menú de pausa
 * antes de implementar el gameplay real con LibGDX.
 */
@Composable
fun GamePlaceholderScreen(
    levelId: Int,
    onNavigateToResult: (GameResultType) -> Unit,
    onRestartLevel: () -> Unit,
    onBackToHome: () -> Unit
) {
    val level = NightLevelsData.getLevelById(levelId)

    var showPauseMenu by remember {
        mutableStateOf(false)
    }

    var showRestartConfirmation by remember {
        mutableStateOf(false)
    }

    var showExitConfirmation by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = NightSpacing.screenHorizontal,
                vertical = NightSpacing.screenVertical
            )
    ) {
        PauseButton(
            onClick = {
                showPauseMenu = true
            },
            modifier = Modifier.align(Alignment.TopEnd)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = NightSpacing.section),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GamePlaceholderHeader(
                title = level?.title ?: "Noche desconocida",
                subtitle = level?.subtitle ?: "Nivel no encontrado"
            )

            Spacer(modifier = Modifier.height(NightSpacing.extraLarge))

            SimulatorInfoCard()

            Spacer(modifier = Modifier.height(NightSpacing.extraLarge))

            when (levelId) {
                0 -> TutorialResultButtons(
                    onNavigateToResult = onNavigateToResult
                )

                4 -> FinalLevelResultButtons(
                    onNavigateToResult = onNavigateToResult
                )

                else -> NormalLevelResultButtons(
                    onNavigateToResult = onNavigateToResult
                )
            }
        }
    }

    GamePlaceholderDialogs(
        showPauseMenu = showPauseMenu,
        showRestartConfirmation = showRestartConfirmation,
        showExitConfirmation = showExitConfirmation,
        onContinue = {
            showPauseMenu = false
        },
        onRestartRequest = {
            showPauseMenu = false
            showRestartConfirmation = true
        },
        onExitRequest = {
            showPauseMenu = false
            showExitConfirmation = true
        },
        onConfirmRestart = {
            showRestartConfirmation = false
            onRestartLevel()
        },
        onDismissRestart = {
            showRestartConfirmation = false
        },
        onConfirmExit = {
            showExitConfirmation = false
            onBackToHome()
        },
        onDismissExit = {
            showExitConfirmation = false
        }
    )
}

@Composable
private fun PauseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Pause,
            contentDescription = "Pausar nivel",
            tint = CheeseYellow
        )
    }
}

@Composable
private fun GamePlaceholderHeader(
    title: String,
    subtitle: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(NightSpacing.extraSmall))

    Text(
        text = subtitle,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun SimulatorInfoCard() {
    NightBaseCard {
        Text(
            text = "Simulador temporal",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(NightSpacing.small))

        Text(
            text = "Selecciona un resultado para probar el flujo de navegación mientras se desarrolla el gameplay real.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun TutorialResultButtons(
    onNavigateToResult: (GameResultType) -> Unit
) {
    val results = listOf(
        "Ganador: 3 estrellas" to GameResultType.TUTORIAL_THREE_STARS,
        "Resultado: 2 estrellas" to GameResultType.TUTORIAL_TWO_STARS,
        "Resultado: 1 estrella" to GameResultType.TUTORIAL_ONE_STAR,
        "Resultado: 0 estrellas" to GameResultType.FIRED
    )

    ResultButtonsRow(
        title = "Resultados del tutorial",
        results = results,
        onNavigateToResult = onNavigateToResult
    )
}

@Composable
private fun NormalLevelResultButtons(
    onNavigateToResult: (GameResultType) -> Unit
) {
    val results = listOf(
        "Ganador" to GameResultType.VICTORY,
        "Sin vidas" to GameResultType.OUT_OF_LIVES,
        "Entregas incompletas" to GameResultType.INCOMPLETE_DELIVERIES
    )

    ResultButtonsRow(
        title = "Resultados de la jornada",
        results = results,
        onNavigateToResult = onNavigateToResult
    )
}

@Composable
private fun FinalLevelResultButtons(
    onNavigateToResult: (GameResultType) -> Unit
) {
    val results = listOf(
        "Ganador final" to GameResultType.FINAL_VICTORY,
        "Sin vidas" to GameResultType.FINAL_DEFEAT,
        "Entregas incompletas" to GameResultType.FINAL_DEFEAT
    )

    ResultButtonsRow(
        title = "Resultados de la noche final",
        results = results,
        onNavigateToResult = onNavigateToResult
    )
}

@Composable
private fun ResultButtonsRow(
    title: String,
    results: List<Pair<String, GameResultType>>,
    onNavigateToResult: (GameResultType) -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(NightSpacing.medium))

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = NightSpacing.medium,
            alignment = Alignment.CenterHorizontally
        ),
        contentPadding = PaddingValues(horizontal = NightSpacing.small)
    ) {
        items(results) { result ->
            ResultButton(
                text = result.first,
                onClick = {
                    onNavigateToResult(result.second)
                }
            )
        }
    }
}

@Composable
private fun ResultButton(
    text: String,
    onClick: () -> Unit
) {
    NightPrimaryButton(
        text = text,
        onClick = onClick,
        modifier = Modifier.width(190.dp)
    )
}

@Composable
private fun GamePlaceholderDialogs(
    showPauseMenu: Boolean,
    showRestartConfirmation: Boolean,
    showExitConfirmation: Boolean,
    onContinue: () -> Unit,
    onRestartRequest: () -> Unit,
    onExitRequest: () -> Unit,
    onConfirmRestart: () -> Unit,
    onDismissRestart: () -> Unit,
    onConfirmExit: () -> Unit,
    onDismissExit: () -> Unit
) {
    if (showPauseMenu) {
        PauseMenuDialog(
            onContinue = onContinue,
            onRestart = onRestartRequest,
            onBackToHome = onExitRequest
        )
    }

    if (showRestartConfirmation) {
        NightMessageDialog(
            title = "Reiniciar nivel",
            message = "¿Deseas reiniciar esta noche? Se perderá el progreso actual del nivel.",
            confirmText = "Reiniciar",
            dismissText = "Cancelar",
            icon = Icons.Default.Refresh,
            iconColor = CheeseYellow,
            onConfirm = onConfirmRestart,
            onDismiss = onDismissRestart
        )
    }

    if (showExitConfirmation) {
        NightMessageDialog(
            title = "Volver al mapa",
            message = "¿Deseas abandonar esta noche y volver al mapa principal? Se perderá el progreso actual.",
            confirmText = "Volver al mapa",
            dismissText = "Cancelar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onConfirmExit,
            onDismiss = onDismissExit
        )
    }
}

@Composable
private fun PauseMenuDialog(
    onContinue: () -> Unit,
    onRestart: () -> Unit,
    onBackToHome: () -> Unit
) {
    NightMessageDialog(
        title = "Juego pausado",
        message = "Selecciona una opción para continuar.",
        confirmText = "Continuar",
        dismissText = null,
        icon = Icons.Default.Pause,
        iconColor = CheeseYellow,
        onConfirm = onContinue,
        onDismiss = onContinue,
        additionalContent = {
            Spacer(modifier = Modifier.height(NightSpacing.small))

            NightSecondaryButton(
                text = "Reiniciar nivel",
                onClick = onRestart,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(NightSpacing.small))

            NightSecondaryButton(
                text = "Volver al mapa",
                onClick = onBackToHome,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}