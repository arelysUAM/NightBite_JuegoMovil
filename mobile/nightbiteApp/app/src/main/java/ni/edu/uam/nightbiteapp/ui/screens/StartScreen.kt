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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.ui.components.branding.GameTitle
import ni.edu.uam.nightbiteapp.ui.components.branding.StartBackground
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.feedback.AnimatedLoadingText
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.StartUiState
import ni.edu.uam.nightbiteapp.viewmodel.StartViewModel

@Composable
fun StartScreen(
    viewModel: StartViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: (UserResponse) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val requestId = remember {
        System.currentTimeMillis()
    }

    LaunchedEffect(requestId) {
        viewModel.checkInitialState(requestId)
    }

    LaunchedEffect(uiState, requestId) {
        when (val state = uiState) {
            is StartUiState.NavigateToLogin -> {
                if (state.requestId == requestId) {
                    onNavigateToLogin()
                }
            }

            is StartUiState.NavigateToHome -> {
                if (state.requestId == requestId) {
                    onNavigateToHome(state.user)
                }
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
            requestId = requestId,
            onRetry = {
                viewModel.retry(requestId)
            }
        )
    }
}

@Composable
private fun BoxScope.StartStateContent(
    uiState: StartUiState,
    requestId: Long,
    onRetry: () -> Unit
) {
    when (uiState) {
        is StartUiState.Loading -> {
            if (uiState.requestId == requestId || uiState.requestId == 0L) {
                AnimatedLoadingText(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = NightSpacing.section)
                )
            }
        }

        is StartUiState.ServerError -> {
            if (uiState.requestId == requestId) {
                StartServerErrorDialog(
                    onRetry = onRetry
                )
            }
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