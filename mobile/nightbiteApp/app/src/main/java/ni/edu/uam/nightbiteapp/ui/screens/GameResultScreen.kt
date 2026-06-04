package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.ui.model.GameResultContent
import ni.edu.uam.nightbiteapp.ui.model.GameResultType
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow

/**
 * Pantalla reutilizable para mostrar los resultados de una noche.
 *
 * Los datos mostrados dependen del nivel y del tipo de resultado recibido.
 */
@Composable
fun GameResultScreen(
    resultType: GameResultType,
    content: GameResultContent,
    onRetryLevel: () -> Unit,
    onContinue: () -> Unit,
    onBackToHome: () -> Unit
) {
    val showContinueButton = shouldShowContinueButton(resultType)
    val showRetryButton = shouldShowRetryButton(resultType)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ResultHeader(
            resultType = resultType,
            content = content
        )

        Spacer(modifier = Modifier.height(20.dp))

        content.stars?.let { stars ->
            StarRating(stars = stars)

            Spacer(modifier = Modifier.height(20.dp))
        }

        ResultMessageCard(content = content)

        Spacer(modifier = Modifier.height(20.dp))

        ResultDetailsCard(content = content)

        content.rewardMessage?.let { reward ->
            Spacer(modifier = Modifier.height(20.dp))

            RewardCard(message = reward)
        }

        content.illustrationDescription?.let { description ->
            Spacer(modifier = Modifier.height(20.dp))

            IllustrationPlaceholder(description = description)
        }

        Spacer(modifier = Modifier.height(28.dp))

        if (showContinueButton) {
            Button(
                onClick = onContinue,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = continueButtonText(resultType))
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        if (showRetryButton) {
            Button(
                onClick = onRetryLevel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Intentar nuevamente")
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        OutlinedButton(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver al mapa")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun ResultHeader(
    resultType: GameResultType,
    content: GameResultContent
) {
    val icon = when (resultType) {
        GameResultType.TUTORIAL_THREE_STARS,
        GameResultType.TUTORIAL_TWO_STARS,
        GameResultType.TUTORIAL_ONE_STAR,
        GameResultType.VICTORY,
        GameResultType.FINAL_VICTORY -> Icons.Default.EmojiEvents

        else -> Icons.Default.Warning
    }

    Icon(
        imageVector = icon,
        contentDescription = "Resultado de la jornada",
        tint = CheeseYellow
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = content.title,
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = content.subtitle,
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun StarRating(
    stars: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(3) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Estrella ${index + 1}",
                tint = if (index < stars) {
                    CheeseYellow
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                }
            )
        }
    }
}

@Composable
private fun ResultMessageCard(
    content: GameResultContent
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = content.message,
            modifier = Modifier.padding(PaddingValues(16.dp)),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ResultDetailsCard(
    content: GameResultContent
) {
    if (content.details.isEmpty()) {
        return
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp))
        ) {
            Text(
                text = "Resumen de la jornada",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            content.details.forEach { detail ->
                Text(
                    text = "• $detail",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun RewardCard(
    message: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(PaddingValues(16.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Recompensa",
                tint = CheeseYellow
            )

            Spacer(modifier = Modifier.padding(horizontal = 6.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun IllustrationPlaceholder(
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ilustración pendiente",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun shouldShowContinueButton(
    resultType: GameResultType
): Boolean {
    return when (resultType) {
        GameResultType.TUTORIAL_THREE_STARS,
        GameResultType.VICTORY,
        GameResultType.FINAL_VICTORY -> true

        else -> false
    }
}

private fun shouldShowRetryButton(
    resultType: GameResultType
): Boolean {
    return when (resultType) {
        GameResultType.TUTORIAL_THREE_STARS,
        GameResultType.VICTORY,
        GameResultType.FINAL_VICTORY -> false

        else -> true
    }
}

private fun continueButtonText(
    resultType: GameResultType
): String {
    return when (resultType) {
        GameResultType.FINAL_VICTORY -> "Finalizar"
        else -> "Continuar"
    }
}