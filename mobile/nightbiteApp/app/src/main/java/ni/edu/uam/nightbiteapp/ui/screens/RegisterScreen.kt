package ni.edu.uam.nightbiteapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.NightRegisterCard
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.NeonGreen
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.ui.validation.AccountValidators
import ni.edu.uam.nightbiteapp.viewmodel.RegisterUiState
import ni.edu.uam.nightbiteapp.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    age: Int,
    onBackToLogin: () -> Unit,
    onBackToAgeCheck: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val uiState = registerViewModel.uiState
    val scrollState = rememberScrollState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var usernameTouched by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }
    var confirmPasswordTouched by remember { mutableStateOf(false) }

    var showAllErrors by remember { mutableStateOf(false) }
    var showEmptyFieldsDialog by remember { mutableStateOf(false) }

    val usernameError = if (usernameTouched || showAllErrors) {
        AccountValidators.validateUsername(username)
    } else {
        null
    }

    val emailError = if (emailTouched || showAllErrors) {
        AccountValidators.validateEmail(email)
    } else {
        null
    }

    val passwordError = if (passwordTouched || showAllErrors) {
        AccountValidators.validatePassword(password)
    } else {
        null
    }

    val confirmPasswordError = if (confirmPasswordTouched || showAllErrors) {
        AccountValidators.validateConfirmPassword(
            password = password,
            confirmPassword = confirmPassword
        )
    } else {
        null
    }

    var returnToLoginAfterConfirm by remember { mutableStateOf(true) }
    var showCancelRegisterDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogType by remember { mutableStateOf(RegisterDialogType.None) }

    val hasRegisterData =
        username.isNotBlank() ||
                email.isNotBlank() ||
                password.isNotBlank() ||
                confirmPassword.isNotBlank()

    val allFieldsAreEmpty =
        username.isBlank() &&
                email.isBlank() &&
                password.isBlank() &&
                confirmPassword.isBlank()

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

    BackHandler {
        if (hasRegisterData) {
            returnToLoginAfterConfirm = false
            showCancelRegisterDialog = true
        } else {
            onBackToAgeCheck()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_estampado_morado),
            contentDescription = "Fondo de registro",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 32.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            NightRegisterCard(
                username = username,
                email = email,
                password = password,
                confirmPassword = confirmPassword,

                usernameError = usernameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,

                onUsernameChange = { value ->
                    usernameTouched = true

                    username = value
                        .lowercase()
                        .replace(" ", "")
                },

                onEmailChange = { value ->
                    emailTouched = true

                    email = value
                        .lowercase()
                        .replace(" ", "")
                },

                onPasswordChange = { value ->
                    passwordTouched = true
                    password = value
                },

                onConfirmPasswordChange = { value ->
                    confirmPasswordTouched = true
                    confirmPassword = value
                },

                onRegisterClick = {
                    showAllErrors = true

                    if (allFieldsAreEmpty) {
                        showEmptyFieldsDialog = true
                    } else {
                        val currentUsernameError =
                            AccountValidators.validateUsername(username)

                        val currentEmailError =
                            AccountValidators.validateEmail(email)

                        val currentPasswordError =
                            AccountValidators.validatePassword(password)

                        val currentConfirmPasswordError =
                            AccountValidators.validateConfirmPassword(
                                password = password,
                                confirmPassword = confirmPassword
                            )

                        val formIsValid =
                            currentUsernameError == null &&
                                    currentEmailError == null &&
                                    currentPasswordError == null &&
                                    currentConfirmPasswordError == null

                        if (formIsValid) {
                            registerViewModel.registerUser(
                                username = username,
                                email = email,
                                password = password,
                                confirmPassword = confirmPassword,
                                age = age
                            )
                        }
                    }
                },

                onBackToLoginClick = {
                    if (hasRegisterData) {
                        returnToLoginAfterConfirm = false
                        showCancelRegisterDialog = true
                    } else {
                        onBackToAgeCheck()
                    }
                },

                modifier = Modifier.widthIn(
                    min = 720.dp,
                    max = 820.dp
                )
            )
        }

        if (uiState is RegisterUiState.Loading) {
            CircularProgressIndicator(
                color = CheeseYellow
            )
        }

        if (showCancelRegisterDialog) {
            NightMessageDialog(
                title = "Cancelar registro",
                message = "¿Seguro quieres volver? Los datos no se guardarán.",
                confirmText = "SÍ, VOLVER",
                dismissText = "CONTINUAR",
                icon = Icons.Default.Warning,
                iconColor = CheeseYellow,
                onConfirm = {
                    showCancelRegisterDialog = false
                    onBackToAgeCheck()
                },
                onDismiss = {
                    showCancelRegisterDialog = false
                }
            )
        }

        if (showEmptyFieldsDialog) {
            NightMessageDialog(
                title = "Campos incompletos",
                message = "Debes completar todos los campos para registrarte.",
                confirmText = "CONTINUAR",
                icon = Icons.Default.Warning,
                iconColor = CheeseYellow,
                onConfirm = {
                    showEmptyFieldsDialog = false
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

private enum class RegisterDialogType {
    None,
    Success,
    Error
}