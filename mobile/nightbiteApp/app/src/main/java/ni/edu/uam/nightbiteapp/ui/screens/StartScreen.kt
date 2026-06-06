package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ni.edu.uam.nightbiteapp.ui.components.AnimatedLoadingText
import ni.edu.uam.nightbiteapp.ui.components.GameTitle
import ni.edu.uam.nightbiteapp.ui.components.StartBackground

/**
 * Pantalla inicial tipo Splash / Loading Screen.
 *
 * Muestra el fondo oficial, logo del juego y texto de carga animado.
 * Al finalizar la carga visual, continúa con el flujo definido en AppNavigation.
 */
@Composable
fun StartScreen(
    onLoadingFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2800)
        onLoadingFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        StartBackground()

        GameTitle(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = 420.dp)
        )

        AnimatedLoadingText(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 34.dp)
        )
    }
}