package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

/**
 * Botón flotante circular para cerrar una tarjeta o pantalla secundaria.
 *
 * Se usa como una X visual sobrepuesta, siguiendo el estilo arcade
 * de la interfaz de NightBite.
 */
@Composable
fun NightFloatingCloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(52.dp)
            .shadow(
                elevation = 8.dp,
                shape = CircleShape
            ),
        shape = CircleShape,
        color = PizzaRed
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cerrar",
                tint = SmokeWhite,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}