package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.R

/**
 * Logo oficial del juego NightBite.
 *
 * Usa titulo_oficial.png ubicado en res/drawable.
 */
@Composable
fun GameTitle(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.titulo_oficial),
        contentDescription = "Logo oficial de NightBite",
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 300.dp),
        contentScale = ContentScale.Fit
    )
}