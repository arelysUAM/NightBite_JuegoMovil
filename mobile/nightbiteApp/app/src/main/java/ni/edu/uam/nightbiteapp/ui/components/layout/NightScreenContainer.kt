package ni.edu.uam.nightbiteapp.ui.components.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ni.edu.uam.nightbiteapp.ui.design.NightDimensions

/**
 * Contenedor base para pantallas completas de NightBite.
 *
 * Une el fondo visual estándar, el ajuste responsivo por tamaño de dispositivo
 * y la opción de scroll en un solo componente reutilizable.
 */
@Composable
fun NightScreenContainer(
    modifier: Modifier = Modifier,
    background: NightBackgroundType = NightBackgroundType.None,
    useScreenPadding: Boolean = true,
    scrollable: Boolean = false,
    avoidKeyboard: Boolean = true,
    content: @Composable BoxScope.(NightDimensions) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        NightBackground(
            type = background,
            modifier = Modifier.fillMaxSize()
        )

        val scrollState = rememberScrollState()

        ResponsiveContentBox(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (avoidKeyboard) {
                        Modifier.imePadding()
                    } else {
                        Modifier
                    }
                )
                .then(
                    if (scrollable) {
                        Modifier.verticalScroll(scrollState)
                    } else {
                        Modifier
                    }
                ),
            useScreenPadding = useScreenPadding,
            content = content
        )
    }
}