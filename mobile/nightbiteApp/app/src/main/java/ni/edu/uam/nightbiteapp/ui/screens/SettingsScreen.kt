package ni.edu.uam.nightbiteapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.design.getNightWindowSize
import ni.edu.uam.nightbiteapp.ui.design.nightDimensionsFor
import ni.edu.uam.nightbiteapp.ui.design.SettingsDimensions
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.ui.theme.SettingsPanelPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsHeaderPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsTabPink
import ni.edu.uam.nightbiteapp.ui.theme.SettingsOptionLavender
import ni.edu.uam.nightbiteapp.ui.components.layout.NightBackgroundType
import ni.edu.uam.nightbiteapp.ui.components.layout.NightScreenContainer

private enum class SettingsTab {
    GAME,
    ACCOUNT
}

@Composable
fun SettingsScreen(
    userSession: UserSession,
    onBackToHome: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onSupportClick: () -> Unit = {},
    onTermsClick: () -> Unit = {}
) {
    var selectedTab by rememberSaveable {
        mutableStateOf(SettingsTab.GAME)
    }

    BackHandler {
        onBackToHome()
    }

    NightScreenContainer(
        background = NightBackgroundType.PurplePattern,
        useScreenPadding = true,
        scrollable = false,
        avoidKeyboard = true
    ) { dimensions ->
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val cardWidth = dimensions.settingsCardWidth.coerceAtMost(maxWidth)
            val cardHeight = dimensions.settingsCardHeight.coerceAtMost(maxHeight)

            Image(
                painter = painterResource(id = R.drawable.boton_volver),
                contentDescription = "Volver",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(dimensions.iconButtonSize)
                    .clickable {
                        onBackToHome()
                    },
                contentScale = ContentScale.Fit
            )

            SettingsCard(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(
                        width = cardWidth,
                        height = cardHeight
                    ),
                selectedTab = selectedTab,
                userSession = userSession,
                innerWidth = dimensions.settingsInnerWidth,
                optionRowWidth = dimensions.settingsOptionRowWidth,
                actionButtonWidth = dimensions.settingsActionButtonWidth,
                onGameTabClick = {
                    selectedTab = SettingsTab.GAME
                },
                onAccountTabClick = {
                    selectedTab = SettingsTab.ACCOUNT
                },
                onNavigateToAccount = onNavigateToAccount,
                onLogout = onLogout,
                onDeleteAccountClick = onDeleteAccountClick,
                onSupportClick = onSupportClick,
                onTermsClick = onTermsClick
            )
        }
    }
}

@Composable
private fun SettingsCard(
    modifier: Modifier,
    selectedTab: SettingsTab,
    userSession: UserSession,
    innerWidth: Dp,
    optionRowWidth: Dp,
    actionButtonWidth: Dp,
    onGameTabClick: () -> Unit,
    onAccountTabClick: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onSupportClick: () -> Unit,
    onTermsClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(NightShapes.panel)
            .background(SettingsPanelPink)
    ) {
        SettingsHeader(
            selectedTab = selectedTab,
            onGameTabClick = onGameTabClick,
            onAccountTabClick = onAccountTabClick
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                SettingsTab.GAME -> {
                    GameSettingsContent(
                        innerWidth = innerWidth,
                        optionRowWidth = optionRowWidth,
                        onSupportClick = onSupportClick
                    )
                }

                SettingsTab.ACCOUNT -> {
                    AccountSettingsContent(
                        userSession = userSession,
                        innerWidth = innerWidth,
                        optionRowWidth = optionRowWidth,
                        actionButtonWidth = actionButtonWidth,
                        onNavigateToAccount = onNavigateToAccount,
                        onLogout = onLogout,
                        onDeleteAccountClick = onDeleteAccountClick,
                        onTermsClick = onTermsClick
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsHeader(
    selectedTab: SettingsTab,
    onGameTabClick: () -> Unit,
    onAccountTabClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(SettingsDimensions.headerHeight)
            .background(SettingsHeaderPink)
            .padding(
                start = NightSpacing.large,
                end = NightSpacing.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Configuraciones",
            color = SmokeWhite,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            modifier = Modifier.weight(1f)
        )

        SettingsTabButton(
            selected = selectedTab == SettingsTab.GAME,
            onClick = onGameTabClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Opciones del juego",
                tint = SmokeWhite
            )
        }

        Spacer(modifier = Modifier.width(NightSpacing.small))

        SettingsTabButton(
            selected = selectedTab == SettingsTab.ACCOUNT,
            onClick = onAccountTabClick
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Cuenta",
                tint = SmokeWhite
            )
        }
    }
}

@Composable
private fun SettingsTabButton(
    selected: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val buttonSize = if (selected) {
        SettingsDimensions.selectedTabButtonSize
    } else {
        SettingsDimensions.tabButtonSize
    }

    val iconSize = if (selected) {
        SettingsDimensions.selectedTabIconSize
    } else {
        SettingsDimensions.tabIconSize
    }

    Box(
        modifier = Modifier
            .size(buttonSize)
            .clip(NightShapes.smallCard)
            .background(
                if (selected) {
                    SettingsPanelPink
                } else {
                    SettingsTabPink
                }
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(iconSize),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
private fun GameSettingsContent(
    innerWidth: Dp,
    optionRowWidth: Dp,
    onSupportClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = NightSpacing.large,
                vertical = NightSpacing.medium
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(1.dp))

        SettingsInnerCard(
            width = innerWidth
        ) {
            Text(
                text = "Opciones del Juego",
                color = SmokeWhite,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(NightSpacing.medium))

            SettingsOptionRow(
                text = "Música",
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                width = optionRowWidth
            )

            SettingsOptionRow(
                text = "Sonido",
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                width = optionRowWidth
            )

            SettingsOptionRow(
                text = "Vibración",
                icon = Icons.Default.Vibration,
                width = optionRowWidth
            )

            SettingsOptionRow(
                text = "Notificaciones",
                icon = Icons.Default.Notifications,
                width = optionRowWidth
            )
        }

        SettingsLink(
            text = "Soporte",
            onClick = onSupportClick
        )
    }
}

@Composable
private fun AccountSettingsContent(
    userSession: UserSession,
    innerWidth: Dp,
    optionRowWidth: Dp,
    actionButtonWidth: Dp,
    onNavigateToAccount: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onTermsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = NightSpacing.large,
                vertical = NightSpacing.medium
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsProfileSummary(
                width = innerWidth,
                username = userSession.username,
                displayId = buildNightBiteDisplayId(
                    userId = userSession.userId ?: 0L,
                    username = userSession.username
                )
            )

            Spacer(modifier = Modifier.height(NightSpacing.small))

            SettingsAccountOptions(
                innerWidth = innerWidth,
                rowWidth = optionRowWidth,
                onNavigateToAccount = onNavigateToAccount
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(NightSpacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingsPillButton(
                text = "CERRAR SESIÓN",
                containerColor = SettingsHeaderPink,
                contentColor = SmokeWhite,
                width = actionButtonWidth,
                onClick = onLogout
            )

            SettingsPillButton(
                text = "ELIMINAR CUENTA",
                containerColor = SettingsPanelPink,
                contentColor = SettingsHeaderPink,
                borderColor = SettingsHeaderPink,
                width = actionButtonWidth,
                onClick = onDeleteAccountClick
            )
        }

        SettingsLink(
            text = "Términos y políticas",
            onClick = onTermsClick
        )
    }
}

@Composable
private fun SettingsInnerCard(
    width: Dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .width(width)
            .clip(NightShapes.panel)
            .background(NightSurface)
            .padding(
                horizontal = NightSpacing.large,
                vertical = NightSpacing.medium
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

@Composable
private fun SettingsOptionRow(
    text: String,
    icon: ImageVector,
    width: Dp
) {
    var checked by remember {
        mutableStateOf(true)
    }

    Row(
        modifier = Modifier
            .width(width)
            .height(SettingsDimensions.optionRowHeight)
            .clip(NightShapes.smallCard)
            .background(SettingsOptionLavender)
            .padding(horizontal = NightSpacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = NightSurface,
            modifier = Modifier.size(SettingsDimensions.optionIconSize)
        )

        Spacer(modifier = Modifier.width(NightSpacing.small))

        Text(
            text = text,
            color = NightSurface,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        SettingsMiniSwitch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
    }

    Spacer(modifier = Modifier.height(NightSpacing.extraSmall))
}

@Composable
private fun SettingsMiniSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .width(SettingsDimensions.switchWidth)
            .height(SettingsDimensions.switchHeight)
            .clip(RoundedCornerShape(50.dp))
            .background(NightSurface)
            .clickable {
                onCheckedChange(!checked)
            }
            .padding(SettingsDimensions.switchPadding),
        contentAlignment = if (checked) {
            Alignment.CenterEnd
        } else {
            Alignment.CenterStart
        }
    ) {
        Box(
            modifier = Modifier
                .size(SettingsDimensions.switchThumbSize)
                .clip(RoundedCornerShape(50.dp))
                .background(SmokeWhite)
        )
    }
}

@Composable
private fun SettingsProfileSummary(
    width: Dp,
    username: String,
    displayId: String
) {
    Column(
        modifier = Modifier
            .width(width)
            .clip(NightShapes.panel)
            .background(NightSurface)
            .padding(
                horizontal = NightSpacing.large,
                vertical = NightSpacing.small
            )
    ) {
        Text(
            text = "Usuario: $username",
            color = SmokeWhite,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "ID: $displayId",
            color = SmokeWhite,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SettingsAccountOptions(
    innerWidth: Dp,
    rowWidth: Dp,
    onNavigateToAccount: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(innerWidth)
            .clip(NightShapes.panel)
            .background(NightSurface)
            .padding(NightSpacing.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsAccountRow(
            text = "Información de la cuenta",
            width = rowWidth,
            onClick = onNavigateToAccount
        )

        SettingsAccountRow(
            text = "Contraseña",
            width = rowWidth,
            onClick = onNavigateToAccount
        )
    }
}

@Composable
private fun SettingsAccountRow(
    text: String,
    width: Dp,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .width(width)
            .height(SettingsDimensions.accountRowHeight)
            .clip(NightShapes.smallCard)
            .background(SettingsOptionLavender)
            .clickable {
                onClick()
            }
            .padding(horizontal = NightSpacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = text,
            tint = NightSurface,
            modifier = Modifier.size(SettingsDimensions.optionIconSize)
        )

        Spacer(modifier = Modifier.width(NightSpacing.small))

        Text(
            text = text,
            color = NightSurface,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Abrir",
            tint = NightSurface,
            modifier = Modifier.size(SettingsDimensions.optionIconSize)
        )
    }

    Spacer(modifier = Modifier.height(NightSpacing.extraSmall))
}

@Composable
private fun SettingsPillButton(
    text: String,
    containerColor: Color,
    contentColor: Color,
    width: Dp,
    borderColor: Color? = null,
    onClick: () -> Unit
) {
    val shape = NightShapes.button

    Box(
        modifier = Modifier
            .width(width)
            .height(SettingsDimensions.actionButtonHeight)
            .clip(shape)
            .background(containerColor)
            .then(
                if (borderColor != null) {
                    Modifier.border(
                        width = SettingsDimensions.actionButtonBorderWidth,
                        color = borderColor,
                        shape = shape
                    )
                } else {
                    Modifier
                }
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun SettingsLink(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        color = NightSurface,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable {
            onClick()
        }
    )
}

private fun buildNightBiteDisplayId(
    userId: Long,
    username: String
): String {
    val numericPart = userId
        .coerceAtLeast(0)
        .toString()
        .padStart(4, '0')

    return "NB-$numericPart-L${username.length}"
}