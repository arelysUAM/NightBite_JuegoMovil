package ni.edu.uam.nightbiteapp.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

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
    onNavigateToSettings: () -> Unit,
    onExitApp: () -> Unit
) {
    val context = LocalContext.current
    val lastBackPressTime = remember { mutableLongStateOf(0L) }

    BackHandler {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastBackPressTime.longValue < 2000) {
            onExitApp()
        } else {
            lastBackPressTime.longValue = currentTime
            Toast.makeText(
                context,
                "Presiona nuevamente para salir",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

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