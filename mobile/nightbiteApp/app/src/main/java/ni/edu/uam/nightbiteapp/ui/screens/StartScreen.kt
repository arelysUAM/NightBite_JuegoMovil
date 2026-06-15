package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.ui.components.feedback.AnimatedLoadingText
import ni.edu.uam.nightbiteapp.ui.components.branding.GameTitle
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.branding.StartBackground
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.StartUiState
import ni.edu.uam.nightbiteapp.viewmodel.StartViewModel

/**
 * Pantalla inicial tipo Splash / Loading Screen.
 *
 * Muestra el fondo oficial, logo del juego y texto de carga animado.
 * Si no se logra conectar con el servidor, muestra un mensaje
 * usando el mismo diálogo visual del resto de la aplicación.
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
                .widthIn(max = NightSizes.registerCardWidth)
        )

        StartStateContent(
            uiState = uiState,
            onRetry = {
                viewModel.retry()
            }
        )
    }
}

@Composable
private fun BoxScope.StartStateContent(
    uiState: StartUiState,
    onRetry: () -> Unit
) {
    when (uiState) {
        is StartUiState.Loading -> {
            AnimatedLoadingText(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = NightSpacing.section)
            )
        }

        is StartUiState.ServerError -> {
            StartServerErrorDialog(
                onRetry = onRetry
            )
        }

        else -> Unit
    }
}

@Composable
private fun StartServerErrorDialog(
    onRetry: () -> Unit
) {
    NightMessageDialog(
        title = "Error de conexión",
        message = "No se pudo conectar con el servidor.",
        confirmText = "Reintentar",
        icon = Icons.Default.Warning,
        iconColor = CheeseYellow,
        onConfirm = onRetry
    )
}