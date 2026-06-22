package ni.edu.uam.nightbiteapp.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.layout.NightBackgroundType
import ni.edu.uam.nightbiteapp.ui.components.layout.NightScreenContainer
import ni.edu.uam.nightbiteapp.ui.design.NightDimensions
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.model.NightLevel
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.viewmodel.HomeViewModel
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Suppress("UNUSED_PARAMETER")
@Composable
fun HomeScreen(
    userId: Long?,
    userSession: UserSession,
    onNavigateToLevelIntro: (Int) -> Unit,
    onNavigateToPlayerDetail: () -> Unit,
    onNavigateToPlayerCreation: () -> Unit,
    onNavigateToAchievements: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit,
    onExitApp: () -> Unit,
    homeViewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val lastBackPressTime = remember {
        mutableLongStateOf(0L)
    }

    val uiState by homeViewModel.uiState.collectAsState()
    val resolvedUserId = userId ?: userSession.userId

    val lifecycleOwner = LocalLifecycleOwner.current

    var showMissingPlayerDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(resolvedUserId) {
        homeViewModel.loadHomeData(resolvedUserId)
    }

    DisposableEffect(lifecycleOwner, resolvedUserId) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                homeViewModel.loadHomeData(resolvedUserId)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BackHandler {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastBackPressTime.longValue < EXIT_PRESS_INTERVAL) {
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

    NightScreenContainer(
        background = NightBackgroundType.None,
        useScreenPadding = false,
        scrollable = false,
        avoidKeyboard = false
    ) { dimensions ->
        val layout = remember(dimensions) {
            homeLayoutFor(dimensions)
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeBackground()

            HomeMissionHeader(
                layout = layout,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = layout.headerTopPadding,
                        end = layout.headerEndPadding
                    )
            )

            HomeSideMenu(
                layout = layout,
                onOpenSettings = onNavigateToSettings,
                onOpenAchievements = onNavigateToAchievements,
                onOpenPlayer = {
                    when {
                        uiState.hasPlayer -> {
                            onNavigateToPlayerDetail()
                        }

                        uiState.userLoadFailed -> {
                            homeViewModel.loadHomeData(resolvedUserId)
                        }

                        else -> {
                            showMissingPlayerDialog = true
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = layout.sideMenuTopPadding,
                        end = layout.sideMenuEndPadding
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(
                        start = layout.levelsHorizontalPadding,
                        end = layout.levelsHorizontalPadding,
                        bottom = layout.levelsBottomPadding
                    )
                    .height(layout.levelsSectionHeight),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = CheeseYellow
                    )
                } else {
                    HomeLevelsRow(
                        levels = uiState.levels,
                        layout = layout,
                        onOpenLevel = { levelId ->
                            when {
                                uiState.hasPlayer -> {
                                    onNavigateToLevelIntro(levelId)
                                }

                                uiState.userLoadFailed -> {
                                    homeViewModel.loadHomeData(resolvedUserId)
                                }

                                else -> {
                                    showMissingPlayerDialog = true
                                }
                            }
                        }
                    )
                }
            }

            if (showMissingPlayerDialog) {
                MissingPlayerDialog(
                    onCreatePlayer = {
                        showMissingPlayerDialog = false
                        onNavigateToPlayerCreation()
                    },
                    onDismiss = {
                        showMissingPlayerDialog = false
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeBackground() {
    val backgroundFrames = remember {
        listOf(
            R.drawable.secuencia1,
            R.drawable.secuencia2,
            R.drawable.secuencia3,
            R.drawable.secuencia4,
            R.drawable.secuencia5,
            R.drawable.secuencia6,
            R.drawable.secuencia7,
            R.drawable.secuencia8,
            R.drawable.secuencia9,
            R.drawable.secuencia10,
            R.drawable.secuencia11,
            R.drawable.secuencia12,
            R.drawable.secuencia13,
            R.drawable.secuencia14,
            R.drawable.secuencia15,
            R.drawable.secuencia16,
            R.drawable.secuencia17,
            R.drawable.secuencia18
        )
    }

    var currentFrame by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(250L)
            currentFrame = (currentFrame + 1) % backgroundFrames.size
        }
    }

    Image(
        painter = painterResource(id = backgroundFrames[currentFrame]),
        contentDescription = "Fondo animado del menú principal",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun HomeMissionHeader(
    layout: HomeLayout,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(layout.headerWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "MISIÓN NOCTURNA",
            textAlign = TextAlign.Center,
            maxLines = 1,
            fontFamily = LilitaOne,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = layout.headerTitleSize,
                color = SmokeWhite,
                shadow = Shadow(
                    color = SmokeWhite.copy(alpha = 0.55f),
                    offset = Offset(2f, 2f),
                    blurRadius = 2f
                )
            )
        )

        Text(
            text = "Entrega todos los pedidos antes de que amanezca",
            textAlign = TextAlign.Center,
            maxLines = 1,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = layout.headerSubtitleSize,
                color = SmokeWhite.copy(alpha = 0.95f)
            )
        )
    }
}

@Composable
private fun HomeSideMenu(
    layout: HomeLayout,
    onOpenSettings: () -> Unit,
    onOpenAchievements: () -> Unit,
    onOpenPlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(layout.sideMenuWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(layout.sideMenuSpacing)
    ) {
        HomeSideMenuItem(
            drawableId = R.drawable.boton_configuracion,
            label = "Configuraciones",
            iconSize = layout.sideMenuIconSize,
            onClick = onOpenSettings
        )

        HomeSideMenuItem(
            drawableId = R.drawable.boton_logros,
            label = "Logros",
            iconSize = layout.sideMenuIconSize,
            onClick = onOpenAchievements
        )

        HomeSideMenuItem(
            drawableId = R.drawable.boton_planilla,
            label = "Plantilla",
            iconSize = layout.sideMenuIconSize,
            onClick = onOpenPlayer
        )
    }
}

@Composable
private fun HomeSideMenuItem(
    drawableId: Int,
    label: String,
    iconSize: Dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(iconSize),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = label,
            modifier = Modifier
                .size(iconSize)
                .clickable {
                    onClick()
                },
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun HomeLevelsRow(
    levels: List<NightLevel>,
    layout: HomeLayout,
    onOpenLevel: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .widthIn(max = layout.levelsRowMaxWidth)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        levels
            .sortedBy { level -> level.id }
            .take(MAX_VISIBLE_LEVELS)
            .forEach { level ->
                HomeLevelItem(
                    level = level,
                    layout = layout,
                    onClick = {
                        if (level.isUnlocked) {
                            onOpenLevel(level.id)
                        }
                    }
                )
            }
    }
}

@Composable
private fun HomeLevelItem(
    level: NightLevel,
    layout: HomeLayout,
    onClick: () -> Unit
) {
    val platformDrawable = if (level.isUnlocked) {
        R.drawable.nivel_completo
    } else {
        R.drawable.nivel_incompleto
    }

    val shieldDrawable = if (level.isUnlocked) {
        levelShieldDrawable(level.id)
    } else {
        R.drawable.nivel_bloqueado
    }

    Box(
        modifier = Modifier
            .width(layout.levelItemWidth)
            .height(layout.levelItemHeight)
            .clickable(
                enabled = level.isUnlocked
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = platformDrawable),
            contentDescription = "Base del nivel ${level.id + 1}",
            modifier = Modifier
                .width(layout.levelPlatformWidth)
                .height(layout.levelPlatformHeight)
                .align(Alignment.Center)
                .offset(y = layout.platformOffsetY),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = painterResource(id = shieldDrawable),
            contentDescription = "Escudo del nivel ${level.id + 1}",
            modifier = Modifier
                .width(layout.levelShieldWidth)
                .height(layout.levelShieldHeight)
                .align(Alignment.Center)
                .offset(y = layout.shieldOffsetY),
            contentScale = ContentScale.Fit
        )

        if (level.isUnlocked) {
            Image(
                painter = painterResource(
                    id = starsDrawable(level.stars)
                ),
                contentDescription = "Estrellas del nivel ${level.id + 1}",
                modifier = Modifier
                    .width(layout.starsWidth)
                    .height(layout.starsHeight)
                    .align(Alignment.BottomCenter)
                    .offset(y = layout.starsOffsetY),
                contentScale = ContentScale.Fit
            )
        }
    }
}

private fun levelShieldDrawable(levelId: Int): Int {
    return when (levelId) {
        0 -> R.drawable.escudo1
        1 -> R.drawable.escudo2
        2 -> R.drawable.escudo3
        3 -> R.drawable.escudo4
        4 -> R.drawable.escudo5
        else -> R.drawable.escudo1
    }
}

private fun starsDrawable(stars: Int): Int {
    return when (stars.coerceIn(0, 3)) {
        1 -> R.drawable.estrellas_1
        2 -> R.drawable.estrellas_2
        3 -> R.drawable.estrellas_3
        else -> R.drawable.estrellas_0
    }
}

@Composable
private fun MissingPlayerDialog(
    onCreatePlayer: () -> Unit,
    onDismiss: () -> Unit
) {
    NightMessageDialog(
        title = "Plantilla requerida",
        message = "Antes de iniciar una jornada debes completar tu plantilla de repartidor.",
        confirmText = "Crear",
        dismissText = "Cancelar",
        icon = Icons.Default.Warning,
        iconColor = CheeseYellow,
        onConfirm = onCreatePlayer,
        onDismiss = onDismiss
    )
}

private fun homeLayoutFor(
    dimensions: NightDimensions
): HomeLayout {
    return when {
        dimensions.contentMaxWidth <= 560.dp -> HomeLayout(
            headerWidth = 190.dp,
            headerTitleSize = 18.sp,
            headerSubtitleSize = 8.sp,
            headerTopPadding = 56.dp,
            headerEndPadding = 72.dp,

            sideMenuWidth = 42.dp,
            sideMenuIconSize = 36.dp,
            sideMenuSpacing = 6.dp,
            sideMenuTopPadding = 16.dp,
            sideMenuEndPadding = 12.dp,

            levelsRowMaxWidth = 530.dp,
            levelsSectionHeight = 120.dp,
            levelsHorizontalPadding = 22.dp,
            levelsBottomPadding = 8.dp,

            levelItemWidth = 98.dp,
            levelItemHeight = 116.dp,

            levelPlatformWidth = 92.dp,
            levelPlatformHeight = 66.dp,

            levelShieldWidth = 50.dp,
            levelShieldHeight = 58.dp,

            platformOffsetY = 10.dp,
            shieldOffsetY = (-10).dp,

            starsWidth = 82.dp,
            starsHeight = 32.dp,
            starsOffsetY = (-8).dp
        )

        dimensions.contentMaxWidth <= 720.dp -> HomeLayout(
            headerWidth = 220.dp,
            headerTitleSize = 22.sp,
            headerSubtitleSize = 9.sp,
            headerTopPadding = 70.dp,
            headerEndPadding = 96.dp,

            sideMenuWidth = 52.dp,
            sideMenuIconSize = 44.dp,
            sideMenuSpacing = 8.dp,
            sideMenuTopPadding = 18.dp,
            sideMenuEndPadding = 16.dp,

            levelsRowMaxWidth = 660.dp,
            levelsSectionHeight = 150.dp,
            levelsHorizontalPadding = 30.dp,
            levelsBottomPadding = 10.dp,

            levelItemWidth = 120.dp,
            levelItemHeight = 140.dp,

            levelPlatformWidth = 108.dp,
            levelPlatformHeight = 78.dp,

            levelShieldWidth = 62.dp,
            levelShieldHeight = 72.dp,

            platformOffsetY = 12.dp,
            shieldOffsetY = (-12).dp,

            starsWidth = 96.dp,
            starsHeight = 38.dp,
            starsOffsetY = (-10).dp
        )

        else -> HomeLayout(
            headerWidth = 260.dp,
            headerTitleSize = 27.sp,
            headerSubtitleSize = 11.sp,
            headerTopPadding = 88.dp,
            headerEndPadding = 142.dp,

            sideMenuWidth = 62.dp,
            sideMenuIconSize = 54.dp,
            sideMenuSpacing = 10.dp,
            sideMenuTopPadding = 22.dp,
            sideMenuEndPadding = 20.dp,

            levelsRowMaxWidth = 820.dp,
            levelsSectionHeight = 190.dp,
            levelsHorizontalPadding = 44.dp,
            levelsBottomPadding = 12.dp,

            levelItemWidth = 148.dp,
            levelItemHeight = 178.dp,

            levelPlatformWidth = 136.dp,
            levelPlatformHeight = 98.dp,

            levelShieldWidth = 76.dp,
            levelShieldHeight = 88.dp,

            platformOffsetY = 16.dp,
            shieldOffsetY = (-16).dp,

            starsWidth = 118.dp,
            starsHeight = 46.dp,
            starsOffsetY = (-12).dp
        )
    }
}

private data class HomeLayout(
    val headerWidth: Dp,
    val headerTitleSize: TextUnit,
    val headerSubtitleSize: TextUnit,
    val headerTopPadding: Dp,
    val headerEndPadding: Dp,

    val sideMenuWidth: Dp,
    val sideMenuIconSize: Dp,
    val sideMenuSpacing: Dp,
    val sideMenuTopPadding: Dp,
    val sideMenuEndPadding: Dp,

    val levelsRowMaxWidth: Dp,
    val levelsSectionHeight: Dp,
    val levelsHorizontalPadding: Dp,
    val levelsBottomPadding: Dp,

    val levelItemWidth: Dp,
    val levelItemHeight: Dp,
    val levelPlatformWidth: Dp,
    val levelPlatformHeight: Dp,
    val levelShieldWidth: Dp,
    val levelShieldHeight: Dp,
    val platformOffsetY: Dp,
    val shieldOffsetY: Dp,

    val starsWidth: Dp,
    val starsHeight: Dp,
    val starsOffsetY: Dp
)

private const val MAX_VISIBLE_LEVELS = 5
private const val EXIT_PRESS_INTERVAL = 2000L