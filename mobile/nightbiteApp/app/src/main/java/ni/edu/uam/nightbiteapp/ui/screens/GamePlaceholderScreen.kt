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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.data.local.mock.NightLevelsData
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.model.GameResultType
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

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
            .padding(20.dp)
    ) {
        IconButton(
            onClick = {
                showPauseMenu = true
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Pause,
                contentDescription = "Pausar nivel"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 44.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = level?.title ?: "Noche desconocida",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = level?.subtitle ?: "Nivel no encontrado",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
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
                        text = "Simulador temporal",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Selecciona un resultado para probar el flujo de navegación mientras se desarrolla el gameplay real."
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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

    if (showPauseMenu) {
        PauseMenuDialog(
            onContinue = {
                showPauseMenu = false
            },
            onRestart = {
                showPauseMenu = false
                showRestartConfirmation = true
            },
            onBackToHome = {
                showPauseMenu = false
                showExitConfirmation = true
            }
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
            onConfirm = {
                showRestartConfirmation = false
                onRestartLevel()
            },
            onDismiss = {
                showRestartConfirmation = false
            }
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
            onConfirm = {
                showExitConfirmation = false
                onBackToHome()
            },
            onDismiss = {
                showExitConfirmation = false
            }
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
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(modifier = Modifier.height(12.dp))

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = 12.dp,
            alignment = Alignment.CenterHorizontally
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
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
    Button(
        onClick = onClick,
        modifier = Modifier.width(190.dp),
        contentPadding = PaddingValues(
            horizontal = 12.dp,
            vertical = 12.dp
        )
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center
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
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onRestart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reiniciar nivel"
                )

                Text(text = " Reiniciar nivel")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onBackToHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Volver al mapa"
                )

                Text(text = " Volver al mapa")
            }
        }
    )
}