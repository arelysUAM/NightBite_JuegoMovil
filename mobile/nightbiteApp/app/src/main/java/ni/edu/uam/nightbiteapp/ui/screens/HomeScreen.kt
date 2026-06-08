package ni.edu.uam.nightbiteapp.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.components.NightLevelButton
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.HomeViewModel

/**
 * Menú principal del juego.
 *
 * Muestra los accesos principales y la selección de noches
 * sin requerir desplazamiento vertical.
 */
@Composable
fun HomeScreen(
    userId: Long?,
    onNavigateToLevelIntro: (Int) -> Unit,
    onNavigateToPlayerDetail: () -> Unit,
    onNavigateToPlayerCreation: () -> Unit,
    onNavigateToAchievements: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onExitApp: () -> Unit,
    homeViewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val lastBackPressTime = remember { mutableLongStateOf(0L) }

    val uiState by homeViewModel.uiState.collectAsState()

    var showMissingPlayerDialog by remember {
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
                .padding(20.dp)
        ) {
            IconButton(
                onClick = onNavigateToSettings,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configuración",
                    tint = SmokeWhite
                )
            }

            Column(
                modifier = Modifier.align(Alignment.TopEnd),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        if (uiState.hasPlayer) {
                            onNavigateToPlayerDetail()
                        } else {
                            showMissingPlayerDialog = true
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = "Plantilla del repartidor",
                        tint = SmokeWhite
                    )
                }

                IconButton(
                    onClick = onNavigateToAchievements
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Libro de logros",
                        tint = SmokeWhite
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "NightBite",
                    style = MaterialTheme.typography.headlineLarge,
                    color = SmokeWhite,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Seleccionar noche",
                    style = MaterialTheme.typography.titleMedium,
                    color = SmokeWhite,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = CheeseYellow
                    )
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 12.dp,
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