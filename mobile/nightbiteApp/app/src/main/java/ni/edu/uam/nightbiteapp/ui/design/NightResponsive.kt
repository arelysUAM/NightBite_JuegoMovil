package ni.edu.uam.nightbiteapp.ui.design

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Utilidades simples para adaptar interfaces según el ancho disponible.
 *
 * Más adelante puede evolucionar a WindowSizeClass, pero por ahora permite
 * tomar decisiones básicas sin complicar el proyecto.
 */
enum class NightWindowSize {
    Compact,
    Medium,
    Expanded
}

fun getNightWindowSize(width: Dp): NightWindowSize {
    return when {
        width < 600.dp -> NightWindowSize.Compact
        width < 840.dp -> NightWindowSize.Medium
        else -> NightWindowSize.Expanded
    }
}

@Composable
fun rememberNightContentMaxWidth(windowSize: NightWindowSize): Dp {
    return when (windowSize) {
        NightWindowSize.Compact -> 360.dp
        NightWindowSize.Medium -> 640.dp
        NightWindowSize.Expanded -> 820.dp
    }
}