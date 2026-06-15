package ni.edu.uam.nightbiteapp.ui.components.branding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import ni.edu.uam.nightbiteapp.R

@Composable
fun StartBackground() {
    Image(
        painter = painterResource(id = R.drawable.thumbnail),
        contentDescription = "Fondo de carga de NightBite",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alignment = Alignment.TopStart
    )
}