package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.theme.SettingsCardDark
import ni.edu.uam.nightbiteapp.ui.theme.SettingsCardLight
import ni.edu.uam.nightbiteapp.ui.theme.SettingsCream
import ni.edu.uam.nightbiteapp.ui.theme.SettingsDangerPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsOverlay
import ni.edu.uam.nightbiteapp.ui.theme.SettingsPanelDarkPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsPanelLightPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsPanelPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsTabPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsTextDark
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.ui.utils.UserDisplayIdGenerator
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.filled.Edit

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

    var musicEnabled by remember { mutableStateOf(true) }
    var soundEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    var showPanel by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        showPanel = true
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

        SettingsHomePreview()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SettingsOverlay)
        )

        val panelWidth = if (maxWidth < 760.dp) {
            maxWidth * 0.40f
        } else {
            330.dp
        }

        AnimatedVisibility(
            visible = showPanel,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(durationMillis = 420)
            ) + fadeIn(
                animationSpec = tween(durationMillis = 280)
            ),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            SettingsSidePanel(
                userSession = userSession,
                selectedTab = selectedTab,
                onTabSelected = {
                    selectedTab = it
                },
                musicEnabled = musicEnabled,
                onMusicChange = { musicEnabled = it },
                soundEnabled = soundEnabled,
                onSoundChange = { soundEnabled = it },
                vibrationEnabled = vibrationEnabled,
                onVibrationChange = { vibrationEnabled = it },
                notificationsEnabled = notificationsEnabled,
                onNotificationsChange = { notificationsEnabled = it },
                onNavigateToAccount = onNavigateToAccount,
                onLogoutClick = {
                    showLogoutDialog = true
                },
                onBackToHome = onBackToHome,
                modifier = Modifier
                    .padding(start = 0.dp, top = 8.dp, bottom = 8.dp)
                    .width(panelWidth)
                    .fillMaxHeight()
            )
        }
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
    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(SettingsPanelShape)
                .background(SettingsPanelPink)
                .border(
                    width = 3.dp,
                    color = SettingsPanelDarkPink,
                    shape = SettingsPanelShape
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = SettingsPanelLightPink,
                        shape = RoundedCornerShape(
                            topEnd = 34.dp,
                            bottomEnd = 34.dp
                        )
                    )
                    .padding(start = 18.dp, top = 14.dp, end = 14.dp, bottom = 14.dp)
            ) {
                SettingsHeader()

                Spacer(modifier = Modifier.height(10.dp))

                AnimatedContent(
                    targetState = selectedTab,
                    label = "SettingsContentAnimation"
                ) { tab ->
                    when (tab) {
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

            SettingsVerticalTabs(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
                onBackToHome = onBackToHome,
                modifier = Modifier
                    .width(56.dp)
                    .fillMaxHeight()
                    .padding(top = 48.dp, bottom = 46.dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.boton_volver),
            contentDescription = "Volver",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 20.dp, y = (-16).dp)
                .size(52.dp)
                .clickableWithoutRipple {
                    onBackToHome()
                },
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun SettingsHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.configuracion_rosa),
            contentDescription = "Decoración de configuración",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(74.dp)
                .alpha(0.95f),
            contentScale = ContentScale.Fit
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(38.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(SettingsTabPink)
                .border(
                    width = 2.dp,
                    color = SettingsPanelDarkPink.copy(alpha = 0.55f),
                    shape = RoundedCornerShape(18.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Configuraciones",
                style = MaterialTheme.typography.headlineSmall,
                color = SmokeWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SettingsVerticalTabs(
    selectedTab: SettingsTab,
    onTabSelected: (SettingsTab) -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsIconTab(
            icon = Icons.Default.VolumeUp,
            selected = selectedTab == SettingsTab.VOLUME,
            contentDescription = "Opciones de volumen",
            onClick = {
                onTabSelected(SettingsTab.VOLUME)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsIconTab(
            icon = Icons.Default.Person,
            selected = selectedTab == SettingsTab.PROFILE,
            contentDescription = "Perfil",
            onClick = {
                onTabSelected(SettingsTab.PROFILE)
            }
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun SettingsIconTab(
    icon: ImageVector,
    selected: Boolean,
    contentDescription: String,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.08f else 1f,
        animationSpec = tween(durationMillis = 180),
        label = "SettingsIconScale"
    )

    Box(
        modifier = Modifier
            .size(44.dp)
            .scale(scale)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (selected) SettingsPanelLightPink else SettingsTabPink
            )
            .border(
                width = 2.dp,
                color = if (selected) SmokeWhite else SettingsPanelDarkPink.copy(alpha = 0.55f),
                shape = RoundedCornerShape(10.dp)
            )
            .clickableWithoutRipple {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = SmokeWhite,
            modifier = Modifier.size(26.dp)
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
    SettingsDarkCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        SettingsSwitchRow(
            title = "Música",
            checked = musicEnabled,
            onCheckedChange = onMusicChange
        )

        SettingsSwitchRow(
            title = "Sonido",
            checked = soundEnabled,
            onCheckedChange = onSoundChange
        )

        SettingsSwitchRow(
            title = "Vibración",
            checked = vibrationEnabled,
            onCheckedChange = onVibrationChange
        )

        SettingsSwitchRow(
            title = "Notificaciones",
            checked = notificationsEnabled,
            onCheckedChange = onNotificationsChange
        )
    }
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

    SettingsDarkCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Datos de la cuenta",
                style = MaterialTheme.typography.titleSmall,
                color = SmokeWhite,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar datos de cuenta",
                tint = SmokeWhite,
                modifier = Modifier
                    .size(30.dp)
                    .clickableWithoutRipple {
                        onNavigateToAccount()
                    }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .height(34.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SettingsDangerPink,
                    contentColor = SettingsPanelDarkPink
                ),
                border = BorderStroke(1.5.dp, SettingsPanelDarkPink.copy(alpha = 0.45f)),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text(
                    text = "Cerrar sesión",
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                // Pendiente: implementar eliminación de cuenta.
                // Por ahora este botón queda solo como elemento visual.
            },
            modifier = Modifier
                .fillMaxWidth(0.74f)
                .height(30.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = SettingsPanelDarkPink
            ),
            border = BorderStroke(1.5.dp, SettingsPanelDarkPink.copy(alpha = 0.65f)),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Text(
                text = "Eliminar cuenta",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .height(34.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SettingsDangerPink,
                contentColor = SettingsPanelDarkPink
            ),
            border = BorderStroke(1.5.dp, SettingsPanelDarkPink.copy(alpha = 0.45f)),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Text(
                text = "Cerrar sesión",
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SettingsDarkCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(SettingsCardDark)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = SmokeWhite,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .width(44.dp)
                .height(28.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = SmokeWhite,
                checkedTrackColor = SettingsCardLight,
                uncheckedThumbColor = SmokeWhite,
                uncheckedTrackColor = SettingsPanelDarkPink
            )
        )
    }
}

@Composable
private fun AccountLine(
    label: String,
    value: String
) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.labelSmall,
        color = SettingsTextDark,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SettingsHomePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 32.dp, top = 18.dp, end = 32.dp, bottom = 18.dp)
            .alpha(0.45f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.boton_configuracion),
            contentDescription = "Configuración decorativa",
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(58.dp),
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
                modifier = Modifier.size(58.dp),
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(id = R.drawable.boton_logros),
                contentDescription = "Logros decorativo",
                modifier = Modifier.size(58.dp),
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(start = 190.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "NightBite",
                style = MaterialTheme.typography.headlineLarge,
                color = SmokeWhite,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Seleccionar noche",
                style = MaterialTheme.typography.titleLarge,
                color = SmokeWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}

private val SettingsPanelShape = RoundedCornerShape(
    topStart = 0.dp,
    bottomStart = 0.dp,
    topEnd = 34.dp,
    bottomEnd = 34.dp
)

private fun Modifier.clickableWithoutRipple(
    onClick: () -> Unit
): Modifier {
    return clickable(
        interactionSource = MutableInteractionSource(),
        indication = null,
        onClick = onClick
    )
}