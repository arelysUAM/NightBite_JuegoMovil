package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Pantalla de configuración del juego.
 *
 * Desde aquí el usuario puede acceder al perfil de cuenta
 * y revisar opciones básicas del juego.
 */
@Composable
fun SettingsScreen(
    onNavigateToAccount: () -> Unit,
    onBackToHome: () -> Unit
) {
    var musicEnabled by remember {
        mutableStateOf(true)
    }

    var soundEnabled by remember {
        mutableStateOf(true)
    }

    var vibrationEnabled by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(PaddingValues(16.dp))
            ) {
                Text(
                    text = "Cuenta",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Consulta tus datos de cuenta y actualiza tus credenciales de acceso."
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onNavigateToAccount,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Cuenta")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(PaddingValues(16.dp))
            ) {
                Text(
                    text = "Opciones del juego",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingSwitchItem(
                    title = "Música",
                    checked = musicEnabled,
                    onCheckedChange = {
                        musicEnabled = it
                    }
                )

                SettingSwitchItem(
                    title = "Sonido",
                    checked = soundEnabled,
                    onCheckedChange = {
                        soundEnabled = it
                    }
                )

                SettingSwitchItem(
                    title = "Vibración",
                    checked = vibrationEnabled,
                    onCheckedChange = {
                        vibrationEnabled = it
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(PaddingValues(16.dp))
            ) {
                Text(
                    text = "Acerca de NightBite",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Videojuego móvil 2D de entregas paranormales.")
                Text(text = "Versión: 1.0")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver al menú principal")
        }
    }
}

@Composable
private fun SettingSwitchItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}