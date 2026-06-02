package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Texto principal de interacción en la pantalla inicial.
 *
 * Se presenta como un "PRESS START" estilo arcade, sin borde pesado,
 * para mantener una apariencia más limpia y cercana a videojuegos retro.
 */
@Composable
fun PressStartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Text(
        text = "PRESS START",
        color = Color(0xFFFFD166),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .clickable { onClick() }
            .padding(12.dp)
    )
}