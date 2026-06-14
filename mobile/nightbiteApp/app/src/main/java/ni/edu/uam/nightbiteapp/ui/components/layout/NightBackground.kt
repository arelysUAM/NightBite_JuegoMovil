package ni.edu.uam.nightbiteapp.ui.components.layout

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.theme.NightBackground

/**
 * Fondo reutilizable para las pantallas de NightBite.
 *
 * Permite seleccionar fondos estándar de la app sin repetir Image,
 * painterResource, ContentScale y color base en cada pantalla.
 */
enum class NightBackgroundType {
    None,
    BluePattern,
    PurplePattern,
    StartImage
}

@DrawableRes
private fun backgroundDrawableFor(
    type: NightBackgroundType
): Int? {
    return when (type) {
        NightBackgroundType.None -> null
        NightBackgroundType.BluePattern -> R.drawable.fondo_estampado_azul
        NightBackgroundType.PurplePattern -> R.drawable.fondo_estampado_morado
        NightBackgroundType.StartImage -> R.drawable.thumbnail
    }
}

@Composable
fun NightBackground(
    type: NightBackgroundType,
    modifier: Modifier = Modifier
) {
    val drawableId = backgroundDrawableFor(type)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NightBackground)
    ) {
        if (drawableId != null) {
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = "Fondo de NightBite",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}