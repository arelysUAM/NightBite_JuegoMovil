package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.ui.components.AnimatedLoadingText
import ni.edu.uam.nightbiteapp.ui.components.GameTitle
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.StartBackground
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.StartUiState
import ni.edu.uam.nightbiteapp.viewmodel.StartViewModel

/**
 * Pantalla inicial tipo Splash / Loading Screen.
 *
 * Muestra el fondo oficial, logo del juego y texto de carga animado.
 * Si no hay conexión con el servidor, muestra un diálogo consistente
 * con el resto de mensajes de la aplicación.
 */
@Composable
fun StartScreen(
    viewModel: StartViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: (UserResponse) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkInitialState()
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is StartUiState.NavigateToLogin -> {
                onNavigateToLogin()
            }

            is StartUiState.NavigateToHome -> {
                onNavigateToHome(state.user)
            }

            else -> Unit
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        StartBackground()

        GameTitle(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = 420.dp)
        )

        if (uiState is StartUiState.Loading) {
            AnimatedLoadingText(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 34.dp)
            )
        }

        if (uiState is StartUiState.ServerError) {
            NightMessageDialog(
                title = "Error de conexión",
                message = "No se pudo conectar al servidor.",
                confirmText = "REINTENTAR",
                icon = Icons.Default.Warning,
                iconColor = CheeseYellow,
                onConfirm = {
                    viewModel.retry()
                }
            )
        }
    }
}