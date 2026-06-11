package ni.edu.uam.nightbiteapp.ui.components.sidepanels

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.SettingsCardDark
import ni.edu.uam.nightbiteapp.ui.theme.SettingsCardLight
import ni.edu.uam.nightbiteapp.ui.theme.SettingsDangerPink
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
    var visible by remember { mutableStateOf(false) }
    var logoutAfterClose by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    fun closePanel() {
        visible = false
    }

    fun closePanelAndLogout() {
        logoutAfterClose = true
        visible = false
    }

    BackHandler(enabled = visible) {
        closePanel()
    }

    LaunchedEffect(visible) {
        if (!visible) {
            delay(330)
            onClosed()

            if (logoutAfterClose) {
                onLogoutClick()
            }
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
            val panelWidth = (maxWidth * 0.43f).coerceIn(
                minimumValue = NightSizes.settingsPanelMinWidth,
                maximumValue = NightSizes.settingsPanelMaxWidth
            )

            AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(360)
                ) + fadeIn(animationSpec = tween(220)),
                exit = slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(330)
                ) + fadeOut(animationSpec = tween(220))
            ) {
                SettingsSidePanel(
                    userSession = userSession,
                    onNavigateToAccount = onNavigateToAccount,
                    onLogoutClick = {
                        closePanelAndLogout()
                    },
                    onBack = {
                        closePanel()
                    },
                    modifier = Modifier
                        .width(panelWidth)
                        .fillMaxHeight(0.92f)
                        .clickableWithoutRipple {
                            // Evita cerrar el overlay al tocar dentro del panel.
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
    var selectedTab by remember { mutableStateOf(SettingsTab.VOLUME) }

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
                .padding(
                    top = NightSpacing.large,
                    bottom = NightSpacing.large
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topEnd = 54.dp,
                            bottomEnd = 54.dp
                        )
                    )
                    .background(SettingsPanelLightPink)
                    .padding(
                        start = NightSpacing.cardHorizontal,
                        top = NightSpacing.none,
                        end = NightSpacing.extraLarge,
                        bottom = NightSpacing.medium
                    )
            ) {
                SettingsHeader()

                Spacer(modifier = Modifier.height(NightSpacing.large))

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
            }

            SettingsTabsColumn(
                selectedTab = selectedTab,
                onTabSelected = {
                    selectedTab = it
                },
                modifier = Modifier
                    .width(72.dp)
                    .fillMaxHeight()
                    .padding(
                        top = 52.dp,
                        start = 10.dp,
                        end = 10.dp
                    )
            )
        }

        Image(
            painter = painterResource(id = R.drawable.boton_volver),
            contentDescription = "Volver",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 31.dp, y = (-14).dp)
                .size(NightSizes.iconLarge)
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
            .height(78.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.configuracion_rosa),
            contentDescription = "Decoración de configuración",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-8).dp)
                .size(70.dp),
            contentScale = ContentScale.Fit
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 5.dp)
                .fillMaxWidth()
                .height(42.dp)
                .clip(NightShapes.levelButton)
                .background(SettingsPanelDarkPink.copy(alpha = 0.82f))
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-2).dp)
                .fillMaxWidth()
                .height(42.dp)
                .clip(NightShapes.levelButton)
                .background(SettingsTabPink),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Configuraciones",
                color = SmokeWhite,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
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
                modifier = Modifier.size(27.dp)
            )
        }

        Spacer(modifier = Modifier.height(NightSpacing.medium))

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
                modifier = Modifier.size(27.dp)
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
        targetValue = if (selected) 1.04f else 0.96f,
        animationSpec = tween(180),
        label = "SettingsIconTabScale"
    )

    Box(
        modifier = Modifier
            .size(52.dp)
            .scale(scale)
            .clip(NightShapes.smallCard)
            .background(
                if (selected) SettingsPanelLightPink else SettingsTabPink
            )
            .clickableWithoutRipple {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        content()
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
        Text(
            text = "Opciones del juego",
            color = SmokeWhite,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = NightSpacing.extraSmall)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(NightShapes.smallCard)
                .background(SettingsCardLight)
                .padding(
                    horizontal = NightSpacing.extraLarge,
                    vertical = NightSpacing.medium
                )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(NightSpacing.extraSmall)
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

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsDarkCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Datos de la cuenta",
                    color = SmokeWhite,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .border(
                            width = 2.2.dp,
                            color = SmokeWhite,
                            shape = RoundedCornerShape(7.dp)
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
                        modifier = Modifier.size(23.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(NightShapes.smallCard)
                    .background(SettingsCardLight)
                    .padding(
                        horizontal = NightSpacing.large,
                        vertical = NightSpacing.medium
                    )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(NightSpacing.extraSmall)
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

        Spacer(modifier = Modifier.height(NightSpacing.medium))

        CenteredPanelButton(
            text = "Cerrar sesión",
            fillFraction = 0.82f,
            height = 34.dp,
            containerColor = SettingsDangerPink.copy(alpha = 0.48f),
            contentColor = SettingsPanelDarkPink,
            borderColor = SettingsPanelDarkPink.copy(alpha = 0.75f),
            onClick = onLogoutClick
        )

        Spacer(modifier = Modifier.height(NightSpacing.small))

        CenteredPanelButton(
            text = "Eliminar cuenta",
            fillFraction = 0.68f,
            height = 30.dp,
            containerColor = Color.Transparent,
            contentColor = SettingsPanelDarkPink,
            borderColor = SettingsPanelDarkPink.copy(alpha = 0.80f),
            onClick = {
                // Pendiente: implementar eliminación de cuenta.
            }
        )
    }
}

@Composable
private fun SettingsDarkCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(NightShapes.dialog)
            .background(SettingsCardDark)
            .padding(
                horizontal = NightSpacing.large,
                vertical = NightSpacing.large
            ),
        verticalArrangement = Arrangement.spacedBy(NightSpacing.medium),
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
            .height(31.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = SettingsTextDark,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.weight(1f)
        )

        MiniSettingsSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun MiniSettingsSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .width(64.dp)
            .height(34.dp)
            .clip(NightShapes.button)
            .background(SettingsCardDark)
            .clickableWithoutRipple {
                onCheckedChange(!checked)
            }
            .padding(NightSpacing.extraSmall),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(SmokeWhite)
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
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelMedium
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
            shape = NightShapes.button,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            border = BorderStroke(2.dp, borderColor),
            contentPadding = PaddingValues(
                horizontal = NightSpacing.small,
                vertical = NightSpacing.none
            )
        ) {
            Text(
                text = text,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

private val SettingsPanelShape = RoundedCornerShape(
    topStart = 0.dp,
    bottomStart = 0.dp,
    topEnd = 58.dp,
    bottomEnd = 58.dp
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