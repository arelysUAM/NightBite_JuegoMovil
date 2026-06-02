package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Componente visual para mostrar el título principal del juego.
 *
 * Se usa en la pantalla inicial para reforzar la identidad del videojuego.
 */
@Composable
fun PixelGameTitle() {
    Text(
        text = "NIGHTBITE",
        color = Color(0xFFFFD166),
        fontSize = 46.sp,
        fontWeight = FontWeight.Black
    )
}