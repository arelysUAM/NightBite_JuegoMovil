package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.SettingsCardDark
import ni.edu.uam.nightbiteapp.ui.theme.SettingsCardLight
import ni.edu.uam.nightbiteapp.ui.theme.SettingsOverlay
import ni.edu.uam.nightbiteapp.ui.theme.SettingsPanelDarkPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsPanelLightPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsPanelPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsTabPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsTextDark
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.ui.utils.UserDisplayIdGenerator

@Composable
fun SettingsPanelOverlay(
    userSession: UserSession,
    onNavigateToAccount: () -> Unit,
    onLogoutClick: () -> Unit,
    onClosed: () -> Unit
) {
    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        visible = true
    }

    fun closePanel() {
        visible = false
    }

    LaunchedEffect(visible) {
        if (!visible) {
            delay(330)
            onClosed()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(180)),
            exit = fadeOut(animationSpec = tween(260))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SettingsOverlay)
                    .clickableWithoutRipple {
                        closePanel()
                    }
            )
        }

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            val panelWidth = if (maxWidth < 760.dp) {
                270.dp
            } else {
                285.dp
            }

            AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(360)
                ) + fadeIn(animationSpec = tween(220)),
                exit = slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(330)
                ) + fadeOut(animationSpec = tween(220))
            ) {
                SettingsSidePanel(
                    userSession = userSession,
                    onNavigateToAccount = onNavigateToAccount,
                    onLogoutClick = onLogoutClick,
                    onBack = {
                        closePanel()
                    },
                    modifier = Modifier
                        .width(panelWidth)
                        .fillMaxHeight()
                        .clickableWithoutRipple {
                            // Evita que los toques dentro del panel cierren el overlay.
                        }
                )
            }
        }
    }
}

@Composable
fun SettingsSidePanel(
    userSession: UserSession,
    onNavigateToAccount: () -> Unit,
    onLogoutClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember {
        mutableStateOf(SettingsTab.VOLUME)
    }

    var musicEnabled by remember { mutableStateOf(true) }
    var soundEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(SettingsPanelShape)
                .background(SettingsPanelPink)
                .border(
                    width = 2.dp,
                    color = SettingsPanelDarkPink,
                    shape = SettingsPanelShape
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topEnd = 32.dp,
                            bottomEnd = 32.dp
                        )
                    )
                    .background(SettingsPanelLightPink)
                    .padding(
                        start = 16.dp,
                        top = 12.dp,
                        end = 12.dp,
                        bottom = 18.dp
                    )
            ) {
                SettingsHeader()

                Spacer(modifier = Modifier.height(10.dp))

                AnimatedContent(
                    targetState = selectedTab,
                    label = "SettingsTabContent"
                ) { tab ->
                    when (tab) {
                        SettingsTab.VOLUME -> {
                            SettingsVolumeContent(
                                musicEnabled = musicEnabled,
                                onMusicChange = { musicEnabled = it },
                                soundEnabled = soundEnabled,
                                onSoundChange = { soundEnabled = it },
                                vibrationEnabled = vibrationEnabled,
                                onVibrationChange = { vibrationEnabled = it },
                                notificationsEnabled = notificationsEnabled,
                                onNotificationsChange = { notificationsEnabled = it }
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

                Spacer(modifier = Modifier.weight(1f))

                if (selectedTab == SettingsTab.VOLUME) {
                    Text(
                        text = "Soporte",
                        color = SettingsTextDark,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 2.dp)
                    )
                }
            }

            SettingsTabsColumn(
                selectedTab = selectedTab,
                onTabSelected = {
                    selectedTab = it
                },
                modifier = Modifier
                    .width(48.dp)
                    .fillMaxHeight()
                    .padding(top = 66.dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.boton_volver),
            contentDescription = "Volver",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 28.dp, y = (-28).dp)
                .size(60.dp)
                .clickableWithoutRipple {
                    onBack()
                },
            contentScale = ContentScale.Fit
        )
    }
}

private enum class SettingsTab {
    VOLUME,
    PROFILE
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
                .size(72.dp),
            contentScale = ContentScale.Fit
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(36.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(SettingsTabPink)
                .border(
                    width = 1.6.dp,
                    color = SettingsPanelDarkPink.copy(alpha = 0.55f),
                    shape = RoundedCornerShape(18.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Configuraciones",
                color = SmokeWhite,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SettingsTabsColumn(
    selectedTab: SettingsTab,
    onTabSelected: (SettingsTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsIconTab(
            selected = selectedTab == SettingsTab.VOLUME,
            onClick = {
                onTabSelected(SettingsTab.VOLUME)
            },
            contentDescription = "Volumen"
        ) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = null,
                tint = SmokeWhite,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        SettingsIconTab(
            selected = selectedTab == SettingsTab.PROFILE,
            onClick = {
                onTabSelected(SettingsTab.PROFILE)
            },
            contentDescription = "Perfil"
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = SmokeWhite,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun SettingsIconTab(
    selected: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
    content: @Composable () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.08f else 1f,
        animationSpec = tween(180),
        label = "SettingsIconTabScale"
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
                color = if (selected) SmokeWhite else SettingsPanelDarkPink.copy(alpha = 0.45f),
                shape = RoundedCornerShape(10.dp)
            )
            .clickableWithoutRipple {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            content()
        }
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
    SettingsDarkCard {
        Text(
            text = "Opciones del Juego",
            color = SmokeWhite,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

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
    val displayId = userSession.userId?.let { id ->
        UserDisplayIdGenerator.generate(
            userId = id,
            username = userSession.username
        )
    } ?: "NB-0000-L0"

    SettingsDarkCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Datos de la cuenta",
                color = SmokeWhite,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(
                        width = 1.4.dp,
                        color = SmokeWhite,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickableWithoutRipple {
                        onNavigateToAccount()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar datos de cuenta",
                    tint = SmokeWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(7.dp))
                .background(SettingsCardLight)
                .padding(horizontal = 8.dp, vertical = 7.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                AccountLine(
                    label = "Nombre",
                    value = userSession.username.ifBlank { "Sin usuario" }
                )

                AccountLine(
                    label = "ID",
                    value = displayId
                )

                AccountLine(
                    label = "Correo",
                    value = userSession.email.ifBlank { "Sin correo" }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

    CenteredPanelButton(
        text = "Cerrar sesión",
        fillFraction = 0.80f,
        height = 32.dp,
        containerColor = SettingsDangerPink,
        contentColor = SettingsPanelDarkPink,
        borderColor = SettingsPanelDarkPink.copy(alpha = 0.45f),
        onClick = onLogoutClick
    )

    Spacer(modifier = Modifier.height(7.dp))

    CenteredPanelButton(
        text = "Eliminar cuenta",
        fillFraction = 0.68f,
        height = 28.dp,
        containerColor = Color.Transparent,
        contentColor = SettingsPanelDarkPink,
        borderColor = SettingsPanelDarkPink.copy(alpha = 0.65f),
        onClick = {
            // Pendiente: implementar eliminación de cuenta.
            // Por ahora queda solo como botón visual.
        }
    )
}

@Composable
private fun SettingsDarkCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SettingsCardDark)
            .padding(horizontal = 12.dp, vertical = 11.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
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
            .height(25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = SmokeWhite,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .width(42.dp)
                .height(24.dp),
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
        color = SettingsTextDark,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun CenteredPanelButton(
    text: String,
    fillFraction: Float,
    height: androidx.compose.ui.unit.Dp,
    containerColor: Color,
    contentColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(fillFraction)
                .height(height)
                .defaultMinSize(minHeight = 1.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            border = BorderStroke(1.5.dp, borderColor),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

private val SettingsPanelShape = RoundedCornerShape(
    topStart = 0.dp,
    bottomStart = 0.dp,
    topEnd = 28.dp,
    bottomEnd = 28.dp
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