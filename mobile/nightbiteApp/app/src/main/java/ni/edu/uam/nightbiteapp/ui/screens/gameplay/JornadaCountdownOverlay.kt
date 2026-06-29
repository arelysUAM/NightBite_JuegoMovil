package ni.edu.uam.nightbiteapp.ui.screens.gameplay

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

@Composable
fun JornadaCountdownOverlay(
    countdownText: String,
    modifier: Modifier = Modifier
) {
    val scale = remember(countdownText) {
        Animatable(1.95f)
    }

    val alpha = remember(countdownText) {
        Animatable(0.15f)
    }

    LaunchedEffect(countdownText) {
        scale.snapTo(1.95f)
        alpha.snapTo(0.15f)

        coroutineScope {
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 630,
                        easing = FastOutSlowInEasing
                    )
                )
            }

            launch {
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 280,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        }
    }

    val isStartText = countdownText.contains(
        other = "Inicio",
        ignoreCase = true
    )

    val fontSize = if (isStartText) {
        42.sp
    } else {
        88.sp
    }

    val textColor = if (isStartText) {
        CheeseYellow
    } else {
        SmokeWhite
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.46f))
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null
            ) {
                // Bloquea toques durante el contador.
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = countdownText,
            color = textColor.copy(alpha = 0.16f),
            fontSize = fontSize,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.graphicsLayer {
                scaleX = scale.value * 1.22f
                scaleY = scale.value * 1.22f
                this.alpha = alpha.value
            }
        )

        Text(
            text = countdownText,
            color = textColor.copy(alpha = 0.30f),
            fontSize = fontSize,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.graphicsLayer {
                scaleX = scale.value * 1.10f
                scaleY = scale.value * 1.10f
                this.alpha = alpha.value
            }
        )

        Text(
            text = countdownText,
            color = textColor,
            fontSize = fontSize,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            style = androidx.compose.ui.text.TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.45f),
                    blurRadius = 6f
                )
            ),
            modifier = Modifier.graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                this.alpha = alpha.value
            }
        )
    }
}