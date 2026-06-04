package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.PlayerDetailUiState
import ni.edu.uam.nightbiteapp.viewmodel.PlayerDetailViewModel

/**
 * Pantalla que muestra la ficha completa del repartidor.
 *
 * Esta pantalla pertenece al Player, no a la cuenta del usuario.
 * Aquí se muestran los datos del personaje/repartidor dentro del juego.
 */
@Composable
fun PlayerDetailScreen(
    userId: Long?,
    onBackToHome: () -> Unit,
    onNavigateToPlayerCreation: () -> Unit,
    onEditPlayer: () -> Unit = {},
    playerDetailViewModel: PlayerDetailViewModel = viewModel()
) {
    val uiState = playerDetailViewModel.uiState

    LaunchedEffect(userId) {
        if (userId != null) {
            playerDetailViewModel.loadPlayerDetail(userId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Ficha del repartidor",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        when {
            userId == null -> {
                EmptyPlayerDetailContent(
                    message = "No hay una sesión activa.",
                    buttonText = "Volver al perfil",
                    onButtonClick = onBackToHome
                )
            }

            uiState is PlayerDetailUiState.Loading -> {
                LoadingPlayerDetailContent()
            }

            uiState is PlayerDetailUiState.Success -> {
                PlayerDetailContent(
                    user = uiState.user,
                    onBackToHome = onBackToHome,
                    onNavigateToPlayerCreation = onNavigateToPlayerCreation,
                    onEditPlayer = onEditPlayer
                )
            }

            uiState is PlayerDetailUiState.Error -> {
                EmptyPlayerDetailContent(
                    message = uiState.message,
                    buttonText = "Volver al perfil",
                    onButtonClick = onBackToHome
                )
            }

            else -> {
                LoadingPlayerDetailContent()
            }
        }
    }
}

@Composable
private fun LoadingPlayerDetailContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = CheeseYellow
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Cargando ficha del repartidor...")
    }
}

@Composable
private fun EmptyPlayerDetailContent(
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp)
        )
    }

    Spacer(modifier = Modifier.height(20.dp))

    Button(
        onClick = onButtonClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = buttonText)
    }
}

@Composable
private fun PlayerDetailContent(
    user: UserResponse,
    onBackToHome: () -> Unit,
    onNavigateToPlayerCreation: () -> Unit,
    onEditPlayer: () -> Unit
) {
    val player = user.player

    if (player == null) {
        NightMessageDialog(
            title = "Ficha no disponible",
            message = "Todavía no has creado tu ficha de repartidor.",
            confirmText = "Crear ficha",
            dismissText = "Volver",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onNavigateToPlayerCreation,
            onDismiss = onBackToHome
        )

        return
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp))
        ) {
            Text(
                text = "Contrato NightBite",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "ID de jugador: #${player.id}")
            Text(text = "Apodo: ${player.nickname}")
            Text(text = "Noche máxima alcanzada: Nivel 0")
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp))
        ) {
            Text(
                text = "Datos del repartidor",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Nombre del repartidor: ${player.driverName}")
            Text(text = "Género: ${player.gender}")
            Text(text = "Color de casco: ${player.helmetColor}")
            Text(text = "Tipo de moto: ${player.motorcycleType}")
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp))
        ) {
            Text(
                text = "Datos laborales",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Puesto: Repartidor")
            Text(text = "Jornada laboral: 12:00 a.m. - 3:00 a.m.")
            Text(text = "Restaurante: NightBite Pizza")
            Text(text = "Estado: Contratado")
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onEditPlayer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Editar ficha")
    }

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedButton(
        onClick = onBackToHome,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Volver al menú principal")
    }
}