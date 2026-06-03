package ni.edu.uam.nightbiteapp.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.ui.components.NightLoginCard
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.LoginUiState
import ni.edu.uam.nightbiteapp.viewmodel.LoginViewModel

/**
 * Pantalla visual de inicio de sesión.
 *
 * Muestra el formulario de acceso del usuario en una tarjeta centrada,
 * valida las credenciales mediante el LoginViewModel y permite navegar
 * hacia la verificación de edad antes del registro.
 */
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: () -> Unit,
    onExitApp: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val uiState = loginViewModel.uiState

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var lastBackPressTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Success -> {
                Toast.makeText(
                    context,
                    "Bienvenida, ${uiState.user.username}",
                    Toast.LENGTH_SHORT
                ).show()

                loginViewModel.resetState()
                onNavigateToHome()
            }

            is LoginUiState.Error -> {
                Toast.makeText(
                    context,
                    uiState.message,
                    Toast.LENGTH_SHORT
                ).show()

                loginViewModel.resetState()
            }

            else -> Unit
        }
    }

    BackHandler {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastBackPressTime < 2000) {
            onExitApp()
        } else {
            lastBackPressTime = currentTime
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
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 32.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        NightLoginCard(
            username = username,
            password = password,
            onUsernameChange = { username = it },
            onPasswordChange = { password = it },
            onLoginClick = {
                loginViewModel.loginUser(
                    usernameOrEmail = username,
                    password = password
                )
            },
            onRegisterClick = onNavigateToRegister,
            modifier = Modifier.widthIn(
                min = 340.dp,
                max = 400.dp
            )
        )

        if (uiState is LoginUiState.Loading) {
            CircularProgressIndicator(
                color = CheeseYellow
            )
        }
    }
}