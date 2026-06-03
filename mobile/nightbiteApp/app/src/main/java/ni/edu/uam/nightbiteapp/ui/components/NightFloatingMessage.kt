package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

/**
 * Mensaje flotante temporal utilizado para mostrar errores de validación.
 *
 * Características:
 * - No bloquea la interfaz.
 * - No utiliza Dialog.
 * - Se muestra sobre el contenido.
 * - Desaparece automáticamente desde la pantalla que lo controla.
 */
@Composable
fun NightFloatingMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = PizzaRed,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(
                horizontal = 20.dp,
                vertical = 12.dp
            )
    ) {
        Text(
            text = message,
            color = SmokeWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}