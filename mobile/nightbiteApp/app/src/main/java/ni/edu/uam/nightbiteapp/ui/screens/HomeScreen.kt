package ni.edu.uam.nightbiteapp.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.components.NightLevelButton
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.SettingsPanelOverlay
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.viewmodel.HomeViewModel
import ni.edu.uam.nightbiteapp.ui.model.NightLevel
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.widthIn
import ni.edu.uam.nightbiteapp.ui.design.getNightWindowSize
import ni.edu.uam.nightbiteapp.ui.design.nightDimensionsFor

@Composable
fun HomeScreen(
    userId: Long?,
    userSession: UserSession,
    onNavigateToLevelIntro: (Int) -> Unit,
    onNavigateToPlayerDetail: () -> Unit,
    onNavigateToPlayerCreation: () -> Unit,
    onNavigateToAchievements: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onLogout: () -> Unit,
    onExitApp: () -> Unit,
    homeViewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val lastBackPressTime = remember { mutableLongStateOf(0L) }
    val uiState by homeViewModel.uiState.collectAsState()

    var showMissingPlayerDialog by remember {
        mutableStateOf(false)
    }

    var showSettingsPanel by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(userId) {
        homeViewModel.loadHomeData(userId)
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeBackground()

        HomeContent(
            isLoading = uiState.isLoading,
            levels = uiState.levels,
            onOpenSettings = {
                showSettingsPanel = true
            },
            onOpenPlayer = {
                if (uiState.hasPlayer) {
                    onNavigateToPlayerDetail()
                } else {
                    showMissingPlayerDialog = true
                }
            },
            onOpenAchievements = onNavigateToAchievements,
            onOpenLevel = { levelId ->
                if (uiState.hasPlayer) {
                    onNavigateToLevelIntro(levelId)
                } else {
                    showMissingPlayerDialog = true
                }
            }
        )

        if (showSettingsPanel) {
            SettingsPanelOverlay(
                userSession = userSession,
                onNavigateToAccount = onNavigateToAccount,
                onLogoutClick = onLogout,
                onClosed = {
                    showSettingsPanel = false
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

@Composable
private fun HomeBackground() {
    Image(
        painter = painterResource(id = R.drawable.fondo_estampado_azul),
        contentDescription = "Fondo del menú principal",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun HomeContent(
    isLoading: Boolean,
    levels: List<NightLevel>,
    onOpenSettings: () -> Unit,
    onOpenPlayer: () -> Unit,
    onOpenAchievements: () -> Unit,
    onOpenLevel: (Int) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val windowSize = getNightWindowSize(maxWidth)
        val dimensions = nightDimensionsFor(windowSize)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = dimensions.screenHorizontalPadding,
                    top = dimensions.screenVerticalPadding,
                    end = dimensions.screenHorizontalPadding,
                    bottom = dimensions.screenVerticalPadding
                )
        ) {
            HomeTopActions(
                iconSize = dimensions.iconButtonSize,
                itemSpacing = dimensions.itemSpacing,
                onOpenSettings = onOpenSettings,
                onOpenPlayer = onOpenPlayer,
                onOpenAchievements = onOpenAchievements
            )

            HomeLevelSelector(
                isLoading = isLoading,
                levels = levels,
                onOpenLevel = onOpenLevel,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = dimensions.contentMaxWidth)
                    .align(Alignment.Center)
                    .padding(horizontal = dimensions.sectionSpacing)
            )
        }
    }
}

@Composable
private fun HomeTopActions(
    iconSize: Dp,
    itemSpacing: Dp,
    onOpenSettings: () -> Unit,
    onOpenPlayer: () -> Unit,
    onOpenAchievements: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeImageButton(
            drawableId = R.drawable.boton_configuracion,
            contentDescription = "Configuración",
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(iconSize),
            onClick = onOpenSettings
        )

        Column(
            modifier = Modifier.align(Alignment.TopEnd),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(itemSpacing)
        ) {
            HomeImageButton(
                drawableId = R.drawable.boton_planilla,
                contentDescription = "Plantilla del repartidor",
                modifier = Modifier.size(iconSize),
                onClick = onOpenPlayer
            )

            HomeImageButton(
                drawableId = R.drawable.boton_logros,
                contentDescription = "Libro de logros",
                modifier = Modifier.size(iconSize),
                onClick = onOpenAchievements
            )
        }
    }
}

@Composable
private fun HomeLevelSelector(
    isLoading: Boolean,
    levels: List<NightLevel>,
    onOpenLevel: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HomeTitle()

        Spacer(modifier = Modifier.height(NightSpacing.extraLarge))

        if (isLoading) {
            CircularProgressIndicator(
                color = CheeseYellow
            )
        } else {
            LevelsRow(
                levels = levels,
                onOpenLevel = onOpenLevel
            )
        }
    }
}

@Composable
private fun HomeTitle() {
    Text(
        text = "NightBite",
        style = MaterialTheme.typography.headlineLarge,
        color = SmokeWhite,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(NightSpacing.extraSmall))

    Text(
        text = "Seleccionar noche",
        style = MaterialTheme.typography.titleLarge,
        color = SmokeWhite,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun LevelsRow(
    levels: List<NightLevel>,
    onOpenLevel: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            space = NightSpacing.extraLarge,
            alignment = Alignment.CenterHorizontally
        ),
        contentPadding = PaddingValues(horizontal = NightSpacing.small)
    ) {
        items(levels) { level ->
            NightLevelButton(
                level = level,
                onClick = {
                    onOpenLevel(level.id)
                }
            )
        }
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

@Composable
private fun HomeImageButton(
    drawableId: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = drawableId),
        contentDescription = contentDescription,
        modifier = modifier.clickable {
            onClick()
        },
        contentScale = ContentScale.Fit
    )
}

private const val EXIT_PRESS_INTERVAL = 2000L