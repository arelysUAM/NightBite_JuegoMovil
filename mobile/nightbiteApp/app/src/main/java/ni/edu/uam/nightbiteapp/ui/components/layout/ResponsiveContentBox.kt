package ni.edu.uam.nightbiteapp.ui.components.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ni.edu.uam.nightbiteapp.ui.design.NightDimensions
import ni.edu.uam.nightbiteapp.ui.design.getNightWindowSize
import ni.edu.uam.nightbiteapp.ui.design.nightDimensionsFor

/**
 * Contenedor reutilizable para adaptar el contenido al tamaño del dispositivo.
 *
 * Calcula las dimensiones visuales de NightBite según el ancho disponible
 * y entrega NightDimensions a la pantalla que lo utiliza.
 */
@Composable
fun ResponsiveContentBox(
    modifier: Modifier = Modifier,
    useScreenPadding: Boolean = true,
    content: @Composable BoxScope.(NightDimensions) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val windowSize = getNightWindowSize(maxWidth)
        val dimensions = nightDimensionsFor(windowSize)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (useScreenPadding) {
                        Modifier.padding(
                            PaddingValues(
                                horizontal = dimensions.screenHorizontalPadding,
                                vertical = dimensions.screenVerticalPadding
                            )
                        )
                    } else {
                        Modifier
                    }
                )
        ) {
            content(dimensions)
        }
    }
}