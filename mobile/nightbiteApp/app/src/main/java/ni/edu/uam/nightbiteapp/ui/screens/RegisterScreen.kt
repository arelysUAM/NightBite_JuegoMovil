package ni.edu.uam.nightbiteapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.components.cards.NightRegisterCard
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.layout.NightBackgroundType
import ni.edu.uam.nightbiteapp.ui.components.layout.NightScreenContainer
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.NeonGreen
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.ui.validation.AccountValidators
import ni.edu.uam.nightbiteapp.viewmodel.RegisterUiState
import ni.edu.uam.nightbiteapp.viewmodel.RegisterViewModel
import ni.edu.uam.nightbiteapp.viewmodel.UsernameAvailabilityUiState
@Composable
fun RegisterScreen(
    age: Int,
    onBackToLogin: () -> Unit,
    onBackToAgeCheck: () -> Unit,
    onRegisterSuccess: (username: String, password: String) -> Unit,
    onEmailAlreadyRegistered: (email: String) -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
) {
    val uiState = registerViewModel.uiState
    val usernameAvailabilityState = registerViewModel.usernameAvailabilityState

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
    var showCancelRegisterDialog by remember { mutableStateOf(false) }
    var showEmailAlreadyRegisteredDialog by remember { mutableStateOf(false) }

    var usernameTakenError by remember { mutableStateOf<String?>(null) }

    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogType by remember { mutableStateOf(RegisterDialogType.None) }

    val usernameFormatError = if (usernameTouched || showAllErrors) {
        AccountValidators.validateUsername(username)
    } else {
        null
    }

    val usernameAvailabilityError = when (usernameAvailabilityState) {
        is UsernameAvailabilityUiState.Unavailable -> {
            usernameAvailabilityState.message
        }

        else -> null
    }

    val usernameError =
        usernameFormatError
            ?: usernameAvailabilityError
            ?: usernameTakenError

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

    val usernameSuccess =
        username.isNotBlank() &&
                usernameFormatError == null &&
                usernameTakenError == null &&
                usernameAvailabilityState is UsernameAvailabilityUiState.Available

    val emailSuccess =
        email.isNotBlank() &&
                emailError == null

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

    fun requestBack() {
        if (hasRegisterData) {
            showCancelRegisterDialog = true
        } else {
            onBackToAgeCheck()
        }
    }

    fun validateAndRegister() {
        showAllErrors = true
        usernameTakenError = null

        if (allFieldsAreEmpty) {
            showEmptyFieldsDialog = true
            return
        }

        if (usernameAvailabilityState is UsernameAvailabilityUiState.Checking) {
            return
        }

        val currentUsernameError = AccountValidators.validateUsername(username)
        val currentEmailError = AccountValidators.validateEmail(email)
        val currentPasswordError = AccountValidators.validatePassword(password)
        val currentConfirmPasswordError = AccountValidators.validateConfirmPassword(
            password = password,
            confirmPassword = confirmPassword
        )

        if (usernameAvailabilityState is UsernameAvailabilityUiState.Unavailable) {
            usernameTouched = true
            return
        }

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

    LaunchedEffect(username) {
        val normalizedUsername = username.trim().lowercase()
        val formatError = AccountValidators.validateUsername(normalizedUsername)

        if (normalizedUsername.isBlank() || formatError != null) {
            registerViewModel.clearUsernameAvailability()
        } else {
            registerViewModel.checkUsernameAvailability(normalizedUsername)
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterUiState.Success -> {
                dialogTitle = "Cuenta creada"
                dialogMessage = uiState.message
                dialogType = RegisterDialogType.Success
                registerViewModel.resetState()
            }

            is RegisterUiState.Error -> {
                val message = uiState.message

                when {
                    message.contains("nombre de usuario", ignoreCase = true) &&
                            message.contains("registrado", ignoreCase = true) -> {
                        usernameTouched = true
                        usernameTakenError = "Nombre de usuario no disponible."
                    }

                    message.contains("correo", ignoreCase = true) &&
                            message.contains("registrado", ignoreCase = true) -> {
                        showEmailAlreadyRegisteredDialog = true
                    }

                    else -> {
                        dialogTitle = "No se pudo registrar"
                        dialogMessage = message
                        dialogType = RegisterDialogType.Error
                    }
                }

                registerViewModel.resetState()
            }

            else -> Unit
        }
    }

    BackHandler {
        requestBack()
    }

    NightScreenContainer(
        background = NightBackgroundType.PurplePattern,
        useScreenPadding = true,
        scrollable = true,
        avoidKeyboard = true
    ) { dimensions ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.boton_volver),
                contentDescription = "Volver",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(dimensions.iconButtonSize)
                    .clickable {
                        requestBack()
                    },
                contentScale = ContentScale.Fit
            )

            NightRegisterCard(
                username = username,
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                usernameError = usernameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
                usernameSuccess = usernameSuccess,
                emailSuccess = emailSuccess,
                onUsernameChange = { value ->
                    usernameTouched = true
                    usernameTakenError = null

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
                    validateAndRegister()
                },
                onBackToLoginClick = {
                    requestBack()
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .widthIn(
                        max = dimensions.registerCardWidth
                    )
            )

            if (uiState is RegisterUiState.Loading) {
                CircularProgressIndicator(
                    color = CheeseYellow,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            RegisterDialogs(
                showCancelRegisterDialog = showCancelRegisterDialog,
                onDismissCancelRegisterDialog = {
                    showCancelRegisterDialog = false
                },
                onConfirmCancelRegister = {
                    showCancelRegisterDialog = false
                    onBackToAgeCheck()
                },
                showEmptyFieldsDialog = showEmptyFieldsDialog,
                onDismissEmptyFieldsDialog = {
                    showEmptyFieldsDialog = false
                },
                showEmailAlreadyRegisteredDialog = showEmailAlreadyRegisteredDialog,
                email = email,
                onStartLoginWithEmail = { selectedEmail ->
                    showEmailAlreadyRegisteredDialog = false
                    onEmailAlreadyRegistered(selectedEmail)
                },
                onDismissEmailAlreadyRegistered = {
                    showEmailAlreadyRegisteredDialog = false
                    email = ""
                    emailTouched = false
                },
                dialogType = dialogType,
                dialogTitle = dialogTitle,
                dialogMessage = dialogMessage,
                onSuccessConfirm = {
                    dialogType = RegisterDialogType.None
                    onRegisterSuccess(username, password)
                },
                onErrorConfirm = {
                    dialogType = RegisterDialogType.None
                }
            )
        }
    }
}

@Composable
private fun RegisterDialogs(
    showCancelRegisterDialog: Boolean,
    onDismissCancelRegisterDialog: () -> Unit,
    onConfirmCancelRegister: () -> Unit,
    showEmptyFieldsDialog: Boolean,
    onDismissEmptyFieldsDialog: () -> Unit,
    showEmailAlreadyRegisteredDialog: Boolean,
    email: String,
    onStartLoginWithEmail: (String) -> Unit,
    onDismissEmailAlreadyRegistered: () -> Unit,
    dialogType: RegisterDialogType,
    dialogTitle: String,
    dialogMessage: String,
    onSuccessConfirm: () -> Unit,
    onErrorConfirm: () -> Unit
) {
    if (showCancelRegisterDialog) {
        NightMessageDialog(
            title = "Cancelar registro",
            message = "¿Seguro quieres volver? Los datos no se guardarán.",
            confirmText = "SÍ, VOLVER",
            dismissText = "CONTINUAR",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onConfirmCancelRegister,
            onDismiss = onDismissCancelRegisterDialog
        )
    }

    if (showEmptyFieldsDialog) {
        NightMessageDialog(
            title = "Campos incompletos",
            message = "Debes completar todos los campos para registrarte.",
            confirmText = "CONTINUAR",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onDismissEmptyFieldsDialog
        )
    }

    if (showEmailAlreadyRegisteredDialog) {
        NightMessageDialog(
            title = "Correo ya registrado",
            message = "Este correo ya está ligado a una cuenta. ¿Quieres iniciar sesión?",
            confirmText = "Iniciar sesión",
            dismissText = "Cancelar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = {
                onStartLoginWithEmail(email)
            },
            onDismiss = onDismissEmailAlreadyRegistered
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
                onConfirm = onSuccessConfirm
            )
        }

        RegisterDialogType.Error -> {
            NightMessageDialog(
                title = dialogTitle,
                message = dialogMessage,
                confirmText = "ENTENDIDO",
                icon = Icons.Default.Error,
                iconColor = PizzaRed,
                onConfirm = onErrorConfirm
            )
        }

        RegisterDialogType.None -> Unit
    }
}

private enum class RegisterDialogType {
    None,
    Success,
    Error
}