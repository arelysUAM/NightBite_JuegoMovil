package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.NightRegisterCard
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.NeonGreen
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.viewmodel.RegisterUiState
import ni.edu.uam.nightbiteapp.viewmodel.RegisterViewModel

/**
 * Pantalla visual de registro de cuenta.
 *
 * Permite ingresar los datos necesarios para crear una cuenta de usuario.
 * La edad se recibe desde AgeCheckScreen y se envía al backend junto
 * con los demás datos.
 */
@Composable
fun RegisterScreen(
    age: Int,
    onBackToLogin: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val uiState = registerViewModel.uiState

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCancelRegisterDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogType by remember { mutableStateOf(RegisterDialogType.None) }

    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterUiState.Success -> {
                dialogTitle = "Cuenta creada"
                dialogMessage = uiState.message
                dialogType = RegisterDialogType.Success
                registerViewModel.resetState()
            }

            is RegisterUiState.Error -> {
                dialogTitle = "No se pudo registrar"
                dialogMessage = uiState.message
                dialogType = RegisterDialogType.Error
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
                registerViewModel.registerUser(
                    username = username,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    age = age
                )
            },
            onBackToLoginClick = {
                showCancelRegisterDialog = true
            },
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

        if (showCancelRegisterDialog) {
            NightMessageDialog(
                title = "Cancelar registro",
                message = "¿Deseas terminar el registro y volver al inicio de sesión?",
                confirmText = "SÍ, VOLVER",
                dismissText = "CONTINUAR",
                icon = Icons.Default.Warning,
                iconColor = CheeseYellow,
                onConfirm = {
                    showCancelRegisterDialog = false
                    onBackToLogin()
                },
                onDismiss = {
                    showCancelRegisterDialog = false
                }
            )
        }

        when (dialogType) {
            RegisterDialogType.Success -> {
                NightMessageDialog(
                    title = dialogTitle,
                    message = dialogMessage,
                    confirmText = "CONTINUAR",
                    icon = Icons.Default.CheckCircle,
                    iconColor = NeonGreen,
                    onConfirm = {
                        dialogType = RegisterDialogType.None
                        onBackToLogin()
                    }
                )
            }

            RegisterDialogType.Error -> {
                NightMessageDialog(
                    title = dialogTitle,
                    message = dialogMessage,
                    confirmText = "ENTENDIDO",
                    icon = Icons.Default.Error,
                    iconColor = PizzaRed,
                    onConfirm = {
                        dialogType = RegisterDialogType.None
                    }
                )
            }

            RegisterDialogType.None -> Unit
        }
    }
}

/**
 * Tipo de diálogo mostrado en la pantalla de registro.
 */
private enum class RegisterDialogType {
    None,
    Success,
    Error
}