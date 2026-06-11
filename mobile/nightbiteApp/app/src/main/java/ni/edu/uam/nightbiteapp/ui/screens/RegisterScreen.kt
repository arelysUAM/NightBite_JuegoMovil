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
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.NightRegisterCard
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.NeonGreen
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.ui.validation.AccountValidators
import ni.edu.uam.nightbiteapp.viewmodel.RegisterUiState
import ni.edu.uam.nightbiteapp.viewmodel.RegisterViewModel
import androidx.compose.foundation.layout.BoxWithConstraints
import ni.edu.uam.nightbiteapp.ui.design.getNightWindowSize
import ni.edu.uam.nightbiteapp.ui.design.nightDimensionsFor

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
    var showCancelRegisterDialog by remember { mutableStateOf(false) }

    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogType by remember { mutableStateOf(RegisterDialogType.None) }

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

        if (allFieldsAreEmpty) {
            showEmptyFieldsDialog = true
            return
        }

        val currentUsernameError = AccountValidators.validateUsername(username)
        val currentEmailError = AccountValidators.validateEmail(email)
        val currentPasswordError = AccountValidators.validatePassword(password)
        val currentConfirmPasswordError = AccountValidators.validateConfirmPassword(
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
        requestBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        RegisterBackground()

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            contentAlignment = Alignment.Center
        ) {
            val windowSize = getNightWindowSize(maxWidth)
            val dimensions = nightDimensionsFor(windowSize)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = dimensions.screenHorizontalPadding,
                        vertical = dimensions.screenVerticalPadding
                    ),
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
                        validateAndRegister()
                    },
                    onBackToLoginClick = {
                        requestBack()
                    },
                    modifier = Modifier.widthIn(
                        max = dimensions.contentMaxWidth
                    )
                )
            }
        }

        if (uiState is RegisterUiState.Loading) {
            CircularProgressIndicator(
                color = CheeseYellow
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
            dialogType = dialogType,
            dialogTitle = dialogTitle,
            dialogMessage = dialogMessage,
            onSuccessConfirm = {
                dialogType = RegisterDialogType.None
                onBackToLogin()
            },
            onErrorConfirm = {
                dialogType = RegisterDialogType.None
            }
        )
    }
}

@Composable
private fun RegisterBackground() {
    Image(
        painter = painterResource(id = R.drawable.fondo_estampado_morado),
        contentDescription = "Fondo de registro",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun RegisterDialogs(
    showCancelRegisterDialog: Boolean,
    onDismissCancelRegisterDialog: () -> Unit,
    onConfirmCancelRegister: () -> Unit,
    showEmptyFieldsDialog: Boolean,
    onDismissEmptyFieldsDialog: () -> Unit,
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