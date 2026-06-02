package ni.edu.uam.nightbiteapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.ui.components.NightRegisterCard
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.RegisterUiState
import ni.edu.uam.nightbiteapp.viewmodel.RegisterViewModel

/**
 * Pantalla visual de registro de jugadores.
 *
 * Permite ingresar los datos necesarios para crear una cuenta.
 * En esta etapa se conecta con el RegisterViewModel para registrar
 * jugadores mediante la API.
 */
@Composable
fun RegisterScreen(
    age: Int,
    onBackToLogin: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val uiState = registerViewModel.uiState

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterUiState.Success -> {
                Toast.makeText(
                    context,
                    uiState.message,
                    Toast.LENGTH_SHORT
                ).show()

                registerViewModel.resetState()
                onBackToLogin()
            }

            is RegisterUiState.Error -> {
                Toast.makeText(
                    context,
                    uiState.message,
                    Toast.LENGTH_SHORT
                ).show()

                registerViewModel.resetState()
            }

            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 32.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        NightRegisterCard(
            username = username,
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            onUsernameChange = { username = it },
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onConfirmPasswordChange = { confirmPassword = it },
            onRegisterClick = {
                if (password.isBlank() || confirmPassword.isBlank()) {
                    Toast.makeText(
                        context,
                        "Completa la contraseña y su confirmación.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(
                        context,
                        "Las contraseñas no coinciden.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    registerViewModel.registerPlayer(
                        username = username,
                        email = email
                    )
                }
            },
            onBackToLoginClick = onBackToLogin,
            modifier = Modifier.widthIn(
                min = 620.dp,
                max = 760.dp
            )
        )

        if (uiState is RegisterUiState.Loading) {
            CircularProgressIndicator(
                color = CheeseYellow
            )
        }
    }
}