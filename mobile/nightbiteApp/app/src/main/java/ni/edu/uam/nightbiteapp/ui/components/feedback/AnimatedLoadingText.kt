package ni.edu.uam.nightbiteapp.ui.components.feedback

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

/**
 * Texto animado para la pantalla de carga.
 *
 * Muestra "Cargando jornada" con puntos animados.
 */
@Composable
fun AnimatedLoadingText(
    modifier: Modifier = Modifier
) {
    var dots by remember {
        mutableIntStateOf(1)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(450)
            dots = if (dots >= 4) 1 else dots + 1
        }
    }

    Text(
        text = "Cargando jornada" + ".".repeat(dots),
        modifier = modifier,
        color = SmokeWhite,
        fontSize = 18.sp,
        style = MaterialTheme.typography.titleMedium.copy(
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(2f, 2f),
                blurRadius = 8f
            )
        )
    )
}