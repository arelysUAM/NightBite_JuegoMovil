package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.DarkText
import ni.edu.uam.nightbiteapp.ui.theme.SettingsCream
import ni.edu.uam.nightbiteapp.ui.theme.SettingsCreamDark
import ni.edu.uam.nightbiteapp.ui.theme.SettingsDangerRed
import ni.edu.uam.nightbiteapp.ui.theme.SettingsOverlay
import ni.edu.uam.nightbiteapp.ui.theme.SettingsPanelDarkPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsPanelPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsTabPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsTextDark
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.ui.utils.UserDisplayIdGenerator

/**
 * Pantalla de configuración general de NightBite.
 *
 * Esta versión actualiza únicamente la interfaz visual.
 * La navegación hacia edición de cuenta y el cierre de sesión se mantienen igual.
 */
@Composable
fun SettingsScreen(
    userSession: UserSession,
    onNavigateToAccount: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    onBackToHome: () -> Unit
) {
    var selectedTab by remember {
        mutableStateOf(SettingsTab.VOLUME)
    }

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

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_estampado_azul),
            contentDescription = "Fondo del menú principal",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SettingsOverlay)
        )

        SettingsHomePreview()

        val panelWidth = if (maxWidth < 720.dp) {
            maxWidth * 0.88f
        } else {
            390.dp
        }

        SettingsSidePanel(
            userSession = userSession,
            selectedTab = selectedTab,
            onTabSelected = {
                selectedTab = it
            },
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
            },
            onNavigateToAccount = onNavigateToAccount,
            onLogoutClick = {
                showLogoutDialog = true
            },
            onBackToHome = onBackToHome,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(
                    start = 24.dp,
                    top = 18.dp,
                    bottom = 18.dp
                )
                .width(panelWidth)
                .fillMaxHeight()
        )
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

private enum class SettingsTab {
    VOLUME,
    PROFILE
}

@Composable
private fun SettingsHomePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 32.dp,
                top = 18.dp,
                end = 32.dp,
                bottom = 18.dp
            )
            .alpha(0.35f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.boton_configuracion),
            contentDescription = "Configuración decorativa",
            modifier = Modifier
                .align(Alignment.TopStart)
                .width(58.dp)
                .height(58.dp),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier.align(Alignment.TopEnd),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.boton_planilla),
                contentDescription = "Plantilla decorativa",
                modifier = Modifier
                    .width(58.dp)
                    .height(58.dp),
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(id = R.drawable.boton_logros),
                contentDescription = "Logros decorativo",
                modifier = Modifier
                    .width(58.dp)
                    .height(58.dp),
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "NightBite",
                style = MaterialTheme.typography.headlineLarge,
                color = SmokeWhite,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Seleccionar noche",
                style = MaterialTheme.typography.titleLarge,
                color = SmokeWhite,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SettingsSidePanel(
    userSession: UserSession,
    selectedTab: SettingsTab,
    onTabSelected: (SettingsTab) -> Unit,
    musicEnabled: Boolean,
    onMusicChange: (Boolean) -> Unit,
    soundEnabled: Boolean,
    onSoundChange: (Boolean) -> Unit,
    vibrationEnabled: Boolean,
    onVibrationChange: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    onNavigateToAccount: () -> Unit,
    onLogoutClick: () -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(34.dp),
        color = SettingsPanelPink,
        shadowElevation = 8.dp,
        border = BorderStroke(
            width = 4.dp,
            color = SettingsPanelDarkPink
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(
                    onClick = onBackToHome,
                    modifier = Modifier.weight(0.85f)
                )

                SettingsTabButton(
                    title = "Volumen",
                    selected = selectedTab == SettingsTab.VOLUME,
                    onClick = {
                        onTabSelected(SettingsTab.VOLUME)
                    },
                    modifier = Modifier.weight(1f)
                )

                SettingsTabButton(
                    title = "Perfil",
                    selected = selectedTab == SettingsTab.PROFILE,
                    onClick = {
                        onTabSelected(SettingsTab.PROFILE)
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(26.dp),
                color = SettingsCream,
                border = BorderStroke(
                    width = 3.dp,
                    color = SettingsCreamDark
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(18.dp)
                ) {
                    when (selectedTab) {
                        SettingsTab.VOLUME -> {
                            SettingsVolumeContent(
                                musicEnabled = musicEnabled,
                                onMusicChange = onMusicChange,
                                soundEnabled = soundEnabled,
                                onSoundChange = onSoundChange,
                                vibrationEnabled = vibrationEnabled,
                                onVibrationChange = onVibrationChange,
                                notificationsEnabled = notificationsEnabled,
                                onNotificationsChange = onNotificationsChange
                            )
                        }

                        SettingsTab.PROFILE -> {
                            SettingsProfileContent(
                                userSession = userSession,
                                onNavigateToAccount = onNavigateToAccount,
                                onLogoutClick = onLogoutClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CheeseYellow,
            contentColor = DarkText
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(
            text = "Volver",
            style = MaterialTheme.typography.labelMedium,
            color = DarkText,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SettingsTabButton(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (selected) {
        SettingsCream
    } else {
        SettingsTabPink
    }

    val borderColor = if (selected) {
        CheeseYellow
    } else {
        SettingsPanelDarkPink
    }

    Button(
        onClick = onClick,
        modifier = modifier.height(42.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = SettingsTextDark
        ),
        border = BorderStroke(
            width = 2.dp,
            color = borderColor
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = SettingsTextDark,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SettingsVolumeContent(
    musicEnabled: Boolean,
    onMusicChange: (Boolean) -> Unit,
    soundEnabled: Boolean,
    onSoundChange: (Boolean) -> Unit,
    vibrationEnabled: Boolean,
    onVibrationChange: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit
) {
    Text(
        text = "Opciones del juego",
        style = MaterialTheme.typography.titleLarge,
        color = SettingsTextDark
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = "Activa o desactiva los efectos principales de NightBite.",
        style = MaterialTheme.typography.bodyMedium,
        color = SettingsTextDark.copy(alpha = 0.75f)
    )

    Spacer(modifier = Modifier.height(18.dp))

    SettingsSwitchItem(
        title = "Música",
        description = "Música ambiental durante el juego.",
        checked = musicEnabled,
        onCheckedChange = onMusicChange
    )

    SettingsSwitchItem(
        title = "Sonido",
        description = "Efectos de pasos, pedidos y enemigos.",
        checked = soundEnabled,
        onCheckedChange = onSoundChange
    )

    SettingsSwitchItem(
        title = "Vibración",
        description = "Respuesta háptica en acciones importantes.",
        checked = vibrationEnabled,
        onCheckedChange = onVibrationChange
    )

    SettingsSwitchItem(
        title = "Notificaciones",
        description = "Avisos del juego y recordatorios.",
        checked = notificationsEnabled,
        onCheckedChange = onNotificationsChange
    )
}

@Composable
private fun SettingsProfileContent(
    userSession: UserSession,
    onNavigateToAccount: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val displayId = userSession.userId?.let { userId ->
        UserDisplayIdGenerator.generate(
            userId = userId,
            username = userSession.username
        )
    } ?: "NB-0000-L0"

    Text(
        text = "Datos de la cuenta",
        style = MaterialTheme.typography.titleLarge,
        color = SettingsTextDark
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = "Esta información sirve para identificar tu cuenta dentro del juego.",
        style = MaterialTheme.typography.bodyMedium,
        color = SettingsTextDark.copy(alpha = 0.75f)
    )

    Spacer(modifier = Modifier.height(16.dp))

    AccountInfoBox(
        label = "ID de jugador",
        value = displayId
    )

    AccountInfoBox(
        label = "Usuario",
        value = userSession.username.ifBlank {
            "Sin usuario"
        }
    )

    AccountInfoBox(
        label = "Correo",
        value = userSession.email.ifBlank {
            "Sin correo registrado"
        }
    )

    Spacer(modifier = Modifier.height(12.dp))

    Button(
        onClick = onNavigateToAccount,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CheeseYellow,
            contentColor = DarkText
        )
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Actualizar credenciales"
        )

        Text(
            text = "  Editar datos",
            style = MaterialTheme.typography.labelLarge,
            color = DarkText
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SettingsPanelDarkPink,
                contentColor = SmokeWhite
            ),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Text(
                text = "Cerrar sesión",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = {
                // Pendiente: implementar eliminación de cuenta.
                // Por ahora este botón queda solo como elemento visual.
            },
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SettingsDangerRed,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Text(
                text = "Eliminar cuenta",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AccountInfoBox(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(
                color = Color.White.copy(alpha = 0.55f),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.5.dp,
                color = SettingsCreamDark,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 10.dp
            )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = SettingsTextDark.copy(alpha = 0.75f)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = SettingsTextDark,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.55f),
        border = BorderStroke(
            width = 1.5.dp,
            color = SettingsCreamDark
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 12.dp
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = SettingsTextDark
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = SettingsTextDark.copy(alpha = 0.75f)
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = CheeseYellow,
                    checkedTrackColor = SettingsPanelDarkPink,
                    uncheckedThumbColor = SettingsCreamDark,
                    uncheckedTrackColor = SettingsCream
                )
            )
        }
    }

    HorizontalDivider(
        color = SettingsCreamDark.copy(alpha = 0.25f)
    )
}