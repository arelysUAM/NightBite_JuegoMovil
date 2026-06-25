package ni.edu.uam.nightbiteapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import ni.edu.uam.nightbiteapp.R

@Composable
fun WantedPosterTransitionScreen(
    playerGender: String,
    onTransitionFinished: () -> Unit
) {
    val posterAlpha = remember {
        Animatable(0f)
    }

    BackHandler {
        // Se bloquea el botón atrás durante la transición
        // para que no se salte el efecto visual.
    }

    LaunchedEffect(playerGender) {
        delay(BLACK_SCREEN_BEFORE_POSTER_MILLIS)

        posterAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = FADE_IN_DURATION_MILLIS)
        )

        delay(POSTER_VISIBLE_MILLIS)

        posterAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = FADE_OUT_DURATION_MILLIS)
        )

        onTransitionFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(
                id = wantedPosterDrawableFor(playerGender)
            ),
            contentDescription = "Cartel de búsqueda del repartidor",
            modifier = Modifier
                .fillMaxSize()
                .alpha(posterAlpha.value),
            contentScale = ContentScale.Crop
        )
    }
}

private fun wantedPosterDrawableFor(
    playerGender: String
): Int {
    val normalizedGender = playerGender.trim().lowercase()

    return when {
        normalizedGender.contains("femenino") ||
                normalizedGender.contains("female") ||
                normalizedGender.contains("mujer") -> {
            R.drawable.desaparicion_female
        }

        else -> {
            R.drawable.desapacion_male
        }
    }
}

private const val BLACK_SCREEN_BEFORE_POSTER_MILLIS = 900L
private const val FADE_IN_DURATION_MILLIS = 900
private const val POSTER_VISIBLE_MILLIS = 1800L
private const val FADE_OUT_DURATION_MILLIS = 650