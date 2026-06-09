package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.ui.components.AnimatedLoadingText
import ni.edu.uam.nightbiteapp.ui.components.GameTitle
import ni.edu.uam.nightbiteapp.ui.components.StartBackground
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.viewmodel.StartUiState
import ni.edu.uam.nightbiteapp.viewmodel.StartViewModel
import androidx.compose.runtime.getValue

/**
 * Pantalla inicial tipo Splash / Loading Screen.
 *
 * Muestra el fondo oficial, logo del juego y texto de carga animado.
 * Al finalizar la carga visual, continúa con el flujo definido en AppNavigation.
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

        when (uiState) {
            is StartUiState.Loading -> {
                AnimatedLoadingText(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 34.dp)
                )
            }

            is StartUiState.ServerError -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 34.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No se pudo conectar con el servidor.",
                        color = SmokeWhite
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.retry()
                        }
                    ) {
                        Text("Reintentar")
                    }

                    TextButton(
                        onClick = {
                            viewModel.continueToLogin()
                        }
                    ) {
                        Text("Continuar al login")
                    }
                }
            }

            else -> Unit
        }
    }
}