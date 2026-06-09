package ni.edu.uam.nightbiteapp.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.components.NightLevelButton
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.SettingsPanelOverlay
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.viewmodel.HomeViewModel

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

        if (currentTime - lastBackPressTime.longValue < 2000) {
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
        Image(
            painter = painterResource(id = R.drawable.fondo_estampado_azul),
            contentDescription = "Fondo del menú principal",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 32.dp,
                    top = 18.dp,
                    end = 32.dp,
                    bottom = 18.dp
                )
        ) {
            HomeImageButton(
                drawableId = R.drawable.boton_configuracion,
                contentDescription = "Configuración",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(58.dp),
                onClick = {
                    showSettingsPanel = true
                }
            )

            Column(
                modifier = Modifier.align(Alignment.TopEnd),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HomeImageButton(
                    drawableId = R.drawable.boton_planilla,
                    contentDescription = "Plantilla del repartidor",
                    modifier = Modifier.size(58.dp),
                    onClick = {
                        if (uiState.hasPlayer) {
                            onNavigateToPlayerDetail()
                        } else {
                            showMissingPlayerDialog = true
                        }
                    }
                )

                HomeImageButton(
                    drawableId = R.drawable.boton_logros,
                    contentDescription = "Libro de logros",
                    modifier = Modifier.size(58.dp),
                    onClick = onNavigateToAchievements
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(horizontal = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "NightBite",
                    style = MaterialTheme.typography.headlineLarge,
                    color = SmokeWhite,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Seleccionar noche",
                    style = MaterialTheme.typography.titleLarge,
                    color = SmokeWhite,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = CheeseYellow
                    )
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 20.dp,
                            alignment = Alignment.CenterHorizontally
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(uiState.levels) { level ->
                            NightLevelButton(
                                level = level,
                                onClick = {
                                    if (uiState.hasPlayer) {
                                        onNavigateToLevelIntro(level.id)
                                    } else {
                                        showMissingPlayerDialog = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

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
        NightMessageDialog(
            title = "Plantilla requerida",
            message = "Antes de iniciar una jornada debes completar tu plantilla de repartidor.",
            confirmText = "Crear",
            dismissText = "Cancelar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = {
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