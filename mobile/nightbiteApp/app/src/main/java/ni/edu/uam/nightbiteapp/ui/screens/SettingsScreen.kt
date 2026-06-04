package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


/**
 * Pantalla de configuración general de NightBite.
 *
 * Muestra datos básicos de la cuenta y preferencias del juego.
 */
@Composable
fun SettingsScreen(
    userSession: UserSession,
    onNavigateToAccount: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
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

    var notificationsEnabled by remember {
        mutableStateOf(true)
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        AccountSummaryCard(
            userSession = userSession,
            onNavigateToAccount = onNavigateToAccount,
            onLogoutClick = {
                showLogoutDialog = true
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        GameOptionsCard(
            musicEnabled = musicEnabled,
            onMusicChange = {
                musicEnabled = it
            },
            soundEnabled = soundEnabled,
            onSoundChange = {
                soundEnabled = it
            },
            vibrationEnabled = vibrationEnabled,
            onVibrationChange = {
                vibrationEnabled = it
            },
            notificationsEnabled = notificationsEnabled,
            onNotificationsChange = {
                notificationsEnabled = it
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver al menú principal")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (showLogoutDialog) {
        NightMessageDialog(
            title = "Cerrar sesión",
            message = "¿Deseas cerrar tu sesión y volver al inicio de sesión?",
            confirmText = "Confirmar",
            dismissText = "Cancelar",
            icon = Icons.Default.Logout,
            iconColor = MaterialTheme.colorScheme.error,
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            },
            onDismiss = {
                showLogoutDialog = false
            }
        )
    }
}

@Composable
private fun AccountSummaryCard(
    userSession: UserSession,
    onNavigateToAccount: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp))
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 52.dp)
                ) {
                    Text(
                        text = "Datos de la cuenta",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Usuario: ${userSession.username}")
                    Text(text = "Correo: ${userSession.email}")
                }

                IconButton(
                    onClick = onNavigateToAccount,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Actualizar credenciales"
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                val buttonWidth = (maxWidth - 12.dp) / 2

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onLogoutClick,
                        modifier = Modifier.width(buttonWidth),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text(text = "Cerrar sesión")
                    }

                    OutlinedButton(
                        onClick = {
                            // Pendiente: implementar eliminación de cuenta.
                        },
                        modifier = Modifier.width(buttonWidth),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(text = "Eliminar cuenta")
                    }
                }
            }
        }
    }
}
@Composable
private fun GameOptionsCard(
    musicEnabled: Boolean,
    onMusicChange: (Boolean) -> Unit,
    soundEnabled: Boolean,
    onSoundChange: (Boolean) -> Unit,
    vibrationEnabled: Boolean,
    onVibrationChange: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit
) {
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

            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                val columnWidth = (maxWidth - 24.dp) / 2

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        modifier = Modifier.width(columnWidth)
                    ) {
                        SettingSwitchItem(
                            title = "Música",
                            checked = musicEnabled,
                            onCheckedChange = onMusicChange
                        )

                        SettingSwitchItem(
                            title = "Sonido",
                            checked = soundEnabled,
                            onCheckedChange = onSoundChange
                        )
                    }

                    Column(
                        modifier = Modifier.width(columnWidth)
                    ) {
                        SettingSwitchItem(
                            title = "Vibración",
                            checked = vibrationEnabled,
                            onCheckedChange = onVibrationChange
                        )

                        SettingSwitchItem(
                            title = "Notificaciones",
                            checked = notificationsEnabled,
                            onCheckedChange = onNotificationsChange
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingSwitchItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}