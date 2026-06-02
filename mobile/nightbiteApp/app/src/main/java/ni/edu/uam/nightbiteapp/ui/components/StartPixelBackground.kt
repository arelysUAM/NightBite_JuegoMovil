package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Fondo temporal para la pantalla inicial.
 */
@Composable
fun StartPixelBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1026))
    )
}