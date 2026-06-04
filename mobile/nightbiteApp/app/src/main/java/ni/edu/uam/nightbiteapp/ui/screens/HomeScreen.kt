package ni.edu.uam.nightbiteapp.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.ui.components.NightLevelButton
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.HomeViewModel

/**
 * Pantalla principal del juego.
 *
 * Funciona como menú principal de NightBite.
 * Desde aquí el jugador puede acceder a configuración, ficha del repartidor,
 * libro de logros y selección temporal de noches.
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
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopHomeBar(
                onNavigateToSettings = onNavigateToSettings,
                onNavigateToPlayerDetail = {
                    if (uiState.hasPlayer) {
                        onNavigateToPlayerDetail()
                    } else {
                        showMissingPlayerDialog = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "NightBite",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Menú principal",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            when {
                uiState.isLoading -> {
                    LoadingHomeContent()
                }

                uiState.errorMessage != null -> {
                    HomeMessageCard(
                        title = "No se pudo cargar la sesión",
                        message = uiState.errorMessage ?: "Ocurrió un error inesperado."
                    )
                }

                !uiState.hasPlayer -> {
                    MissingPlayerContent(
                        onNavigateToPlayerCreation = onNavigateToPlayerCreation
                    )
                }

                else -> {
                    ActivePlayerContent()
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (uiState.hasPlayer) {
                        onNavigateToLevelIntro(0)
                    } else {
                        showMissingPlayerDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Continuar jornada")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onNavigateToAchievements,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "Libro de logros"
                )

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                Text(text = "Libro de logros")
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Seleccionar noche",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 12.dp,
                    alignment = Alignment.CenterHorizontally
                ),
                contentPadding = PaddingValues(horizontal = 4.dp)
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

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showMissingPlayerDialog) {
        NightMessageDialog(
            title = "Ficha requerida",
            message = "Antes de iniciar una jornada debes completar tu ficha de repartidor.",
            confirmText = "Crear ficha",
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
private fun TopHomeBar(
    onNavigateToSettings: () -> Unit,
    onNavigateToPlayerDetail: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onNavigateToSettings
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Configuración"
            )
        }

        IconButton(
            onClick = onNavigateToPlayerDetail
        ) {
            Icon(
                imageVector = Icons.Default.Badge,
                contentDescription = "Ficha del repartidor"
            )
        }
    }
}

@Composable
private fun LoadingHomeContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = CheeseYellow
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Cargando menú principal...")
    }
}

@Composable
private fun HomeMessageCard(
    title: String,
    message: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp))
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = message)
        }
    }
}

@Composable
private fun MissingPlayerContent(
    onNavigateToPlayerCreation: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp))
        ) {
            Text(
                text = "Ficha de repartidor pendiente",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Completa tu plantilla de contratación antes de iniciar una jornada nocturna."
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onNavigateToPlayerCreation,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Completar ficha")
            }
        }
    }
}

@Composable
private fun ActivePlayerContent() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp))
        ) {
            Text(
                text = "Jornada nocturna",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Completa entregas, sobrevive a la noche y descubre qué ocurre después de aceptar el contrato."
            )
        }
    }
}