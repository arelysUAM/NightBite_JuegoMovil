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
 * Pantalla base para el registro de nuevos jugadores.
 *
 * Más adelante se conectará con el backend mediante Retrofit.
 */
@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro de jugador")

        Button(onClick = onBackToLogin) {
            Text(text = "Volver al login")
        }
    }
}