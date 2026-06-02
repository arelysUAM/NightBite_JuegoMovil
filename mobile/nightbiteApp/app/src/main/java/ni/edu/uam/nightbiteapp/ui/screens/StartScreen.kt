package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.ui.components.PixelGameTitle
import ni.edu.uam.nightbiteapp.ui.components.PressStartButton
import ni.edu.uam.nightbiteapp.ui.components.StartPixelBackground

/**
 * Pantalla inicial del videojuego.
 *
 * Muestra el título del juego y el botón "PRESS START".
 * Funciona como entrada visual antes del login o menú principal.
 */
@Composable
fun StartScreen(
    onPressStart: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "press_start_animation")

    val buttonAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 850),
            repeatMode = RepeatMode.Reverse
        ),
        label = "press_start_alpha"
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        StartPixelBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PixelGameTitle()

            Text(
                text = "Delivery nocturno",
                color = Color(0xFFB8B8D1),
                modifier = Modifier.padding(top = 8.dp, bottom = 28.dp)
            )

            PressStartButton(
                modifier = Modifier.alpha(buttonAlpha),
                onClick = onPressStart
            )
        }
    }
}