package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import ni.edu.uam.nightbiteapp.ui.components.cards.NightBaseCard
import ni.edu.uam.nightbiteapp.ui.components.buttons.NightPrimaryButton
import ni.edu.uam.nightbiteapp.ui.components.buttons.NightSecondaryButton
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.model.GameResultContent
import ni.edu.uam.nightbiteapp.ui.model.GameResultType
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.unit.Dp
import ni.edu.uam.nightbiteapp.ui.design.getNightWindowSize
import ni.edu.uam.nightbiteapp.ui.design.nightDimensionsFor

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
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ResultHeader(
                    resultType = resultType,
                    content = content,
                    itemSpacing = dimensions.itemSpacing
                )

                Spacer(modifier = Modifier.height(dimensions.sectionSpacing))

                content.stars?.let { stars ->
                    StarRating(stars = stars)

                    Spacer(modifier = Modifier.height(dimensions.sectionSpacing))
                }

                ResultMessageCard(content = content)

                Spacer(modifier = Modifier.height(dimensions.sectionSpacing))

                ResultDetailsCard(content = content)

                content.rewardMessage?.let { reward ->
                    Spacer(modifier = Modifier.height(dimensions.sectionSpacing))

                    RewardCard(message = reward)
                }

                content.illustrationDescription?.let { description ->
                    Spacer(modifier = Modifier.height(dimensions.sectionSpacing))

                    IllustrationPlaceholder(description = description)
                }

                Spacer(modifier = Modifier.height(dimensions.sectionSpacing))

                ResultActions(
                    resultType = resultType,
                    showContinueButton = showContinueButton,
                    showRetryButton = showRetryButton,
                    itemSpacing = dimensions.itemSpacing,
                    onContinue = onContinue,
                    onRetryLevel = onRetryLevel,
                    onBackToHome = onBackToHome
                )

                Spacer(modifier = Modifier.height(dimensions.sectionSpacing))
            }
        }
    }
}

@Composable
private fun ResultHeader(
    resultType: GameResultType,
    content: GameResultContent,
    itemSpacing: Dp
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

    Spacer(modifier = Modifier.height(itemSpacing))

    Text(
        text = content.title,
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(itemSpacing))

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
    NightBaseCard {
        Text(
            text = content.message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
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

    NightBaseCard {
        Text(
            text = "Resumen de la jornada",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(NightSpacing.medium))

        content.details.forEach { detail ->
            Text(
                text = "• $detail",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(NightSpacing.extraSmall))
        }
    }
}

@Composable
private fun RewardCard(
    message: String
) {
    NightBaseCard {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Recompensa",
                tint = CheeseYellow
            )

            Spacer(modifier = Modifier.padding(horizontal = NightSpacing.small))

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
    NightBaseCard {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ilustración pendiente",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(NightSpacing.small))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ResultActions(
    resultType: GameResultType,
    showContinueButton: Boolean,
    showRetryButton: Boolean,
    itemSpacing: Dp,
    onContinue: () -> Unit,
    onRetryLevel: () -> Unit,
    onBackToHome: () -> Unit
) {
    if (showContinueButton) {
        NightPrimaryButton(
            text = continueButtonText(resultType),
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(itemSpacing))
    }

    if (showRetryButton) {
        NightPrimaryButton(
            text = "Intentar nuevamente",
            onClick = onRetryLevel,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(itemSpacing))
    }

    NightSecondaryButton(
        text = "Volver al mapa",
        onClick = onBackToHome,
        modifier = Modifier.fillMaxWidth()
    )
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