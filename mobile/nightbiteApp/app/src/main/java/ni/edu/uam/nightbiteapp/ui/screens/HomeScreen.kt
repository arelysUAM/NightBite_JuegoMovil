package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Pantalla principal del jugador.
 *
 * Desde esta pantalla se navega hacia las secciones principales:
 * juego, perfil y configuración.
 */
@Composable
fun HomeScreen(
    onNavigateToGame: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Menú principal - NightBite")

        Button(onClick = onNavigateToGame) {
            Text(text = "Jugar")
        }

        Button(onClick = onNavigateToProfile) {
            Text(text = "Perfil")
        }

        Button(onClick = onNavigateToSettings) {
            Text(text = "Configuración")
        }
    }
}