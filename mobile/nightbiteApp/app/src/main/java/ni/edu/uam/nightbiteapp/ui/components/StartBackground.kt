package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import ni.edu.uam.nightbiteapp.R

/**
 * Fondo oficial de la pantalla de carga de NightBite.
 *
 * Usa la imagen thumbnail.png ubicada en res/drawable.
 */
@Composable
fun StartBackground() {
    Image(
        painter = painterResource(id = R.drawable.thumbnail),
        contentDescription = "Fondo de carga de NightBite",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}