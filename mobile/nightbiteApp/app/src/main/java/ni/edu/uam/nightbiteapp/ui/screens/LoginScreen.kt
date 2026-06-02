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
 * Pantalla inicial de inicio de sesión.
 *
 * En esta etapa funciona como interfaz base para navegar al registro
 * o al menú principal de la aplicación.
 */
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "NightBite - Login")

        Button(onClick = onNavigateToHome) {
            Text(text = "Entrar")
        }

        Button(onClick = onNavigateToRegister) {
            Text(text = "Crear cuenta")
        }
    }
}