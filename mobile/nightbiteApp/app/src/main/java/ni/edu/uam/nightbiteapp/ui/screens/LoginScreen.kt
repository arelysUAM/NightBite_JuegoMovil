package ni.edu.uam.nightbiteapp.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.data.local.session.SessionManager
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.ui.components.cards.NightLoginCard
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.layout.NightBackgroundType
import ni.edu.uam.nightbiteapp.ui.components.layout.NightScreenContainer
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.NeonGreen
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.viewmodel.LoginErrorType
import ni.edu.uam.nightbiteapp.viewmodel.LoginUiState
import ni.edu.uam.nightbiteapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToHome: (UserResponse) -> Unit,
    onExitApp: () -> Unit,
    initialUsername: String = "",
    initialPassword: String = "",
    loginViewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current

    val sessionManager = remember {
        SessionManager(context.applicationContext)
    }

    val uiState = loginViewModel.uiState

    var username by remember {
        mutableStateOf(initialUsername)
    }

    var password by remember {
        mutableStateOf(initialPassword)
    }

    var lastBackPressTime by remember {
        mutableLongStateOf(0L)
    }

    var loginErrorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var loginErrorType by remember {
        mutableStateOf<LoginErrorType?>(null)
    }

    fun clearLoginError() {
        loginErrorMessage = null
        loginErrorType = null
    }

    fun clearCredentials() {
        username = ""
        password = ""
    }

    LaunchedEffect(initialUsername, initialPassword) {
        username = initialUsername
        password = initialPassword
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Success -> {
                val loggedUser = uiState.user

                sessionManager.saveSession(
                    user = loggedUser
                )

                loginViewModel.resetState()
                onNavigateToHome(loggedUser)
            }

            is LoginUiState.Error -> {
                loginErrorMessage = uiState.message
                loginErrorType = uiState.type
                loginViewModel.resetState()
            }

            else -> Unit
        }
    }

    BackHandler {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastBackPressTime < EXIT_PRESS_INTERVAL) {
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

    NightScreenContainer(
        background = NightBackgroundType.PurplePattern,
        useScreenPadding = true,
        scrollable = true,
        avoidKeyboard = true
    ) { dimensions ->
        NightLoginCard(
            username = username,
            password = password,
            onUsernameChange = { value ->
                username = value
                    .lowercase()
                    .replace(" ", "")
            },
            onPasswordChange = { value ->
                password = value
            },
            onLoginClick = {
                loginViewModel.loginUser(
                    usernameOrEmail = username,
                    password = password
                )
            },
            onRegisterClick = onNavigateToRegister,
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(
                    max = dimensions.cardMaxWidth
                )
        )

        if (uiState is LoginUiState.Loading) {
            CircularProgressIndicator(
                color = CheeseYellow,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        LoginErrorDialog(
            message = loginErrorMessage,
            errorType = loginErrorType,
            onRegister = {
                clearLoginError()
                clearCredentials()
                loginViewModel.resetState()
                onNavigateToRegister()
            },
            onDismissUserNotFound = {
                clearLoginError()
                clearCredentials()
                loginViewModel.resetState()
            },
            onDismissError = {
                clearLoginError()
            }
        )
    }
}

@Composable
private fun LoginErrorDialog(
    message: String?,
    errorType: LoginErrorType?,
    onRegister: () -> Unit,
    onDismissUserNotFound: () -> Unit,
    onDismissError: () -> Unit
) {
    if (message == null) {
        return
    }

    when (errorType) {
        LoginErrorType.UserNotFound -> {
            NightMessageDialog(
                title = "Usuario no encontrado",
                message = message,
                confirmText = "Registrarse",
                dismissText = "Cancelar",
                icon = Icons.Default.PersonAdd,
                iconColor = NeonGreen,
                onConfirm = onRegister,
                onDismiss = onDismissUserNotFound
            )
        }

        LoginErrorType.InvalidCredentials -> {
            NightMessageDialog(
                title = "Credenciales incorrectas",
                message = message,
                confirmText = "VOLVER A INTENTAR",
                icon = Icons.Default.Error,
                iconColor = PizzaRed,
                onConfirm = onDismissError
            )
        }

        LoginErrorType.IncompleteFields -> {
            NightMessageDialog(
                title = "Campos incompletos",
                message = message,
                confirmText = "ENTENDIDO",
                icon = Icons.Default.Warning,
                iconColor = CheeseYellow,
                onConfirm = onDismissError
            )
        }

        LoginErrorType.ConnectionError -> {
            NightMessageDialog(
                title = "Error de conexión",
                message = message,
                confirmText = "VOLVER A INTENTAR",
                icon = Icons.Default.Warning,
                iconColor = CheeseYellow,
                onConfirm = onDismissError
            )
        }

        null -> Unit
    }
}

private const val EXIT_PRESS_INTERVAL = 2000L