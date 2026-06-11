package ni.edu.uam.nightbiteapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.components.NightBaseCard
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.NightPrimaryButton
import ni.edu.uam.nightbiteapp.ui.components.NightSecondaryButton
import ni.edu.uam.nightbiteapp.ui.components.NightTextField
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.AccountCredentialsViewModel

/**
 * Pantalla para actualizar las credenciales de acceso.
 *
 * Permite cambiar el nombre de usuario, la contraseña o ambos.
 * Después de aplicar cambios correctamente, se limpia la sesión
 * y el usuario debe iniciar sesión nuevamente.
 */
@Composable
fun AccountScreen(
    userSession: UserSession,
    viewModel: AccountCredentialsViewModel,
    onBackToSettings: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var showExitConfirmation by remember {
        mutableStateOf(false)
    }

    val hasUnsavedChanges =
        uiState.newUsername.isNotEmpty() ||
                uiState.currentPassword.isNotEmpty() ||
                uiState.newPassword.isNotEmpty() ||
                uiState.confirmNewPassword.isNotEmpty()

    fun requestExit() {
        if (uiState.isLoading) {
            return
        }

        if (hasUnsavedChanges) {
            showExitConfirmation = true
        } else {
            onBackToSettings()
        }
    }

    BackHandler {
        requestExit()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = NightSpacing.screenHorizontal,
                vertical = NightSpacing.screenVertical
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AccountHeader()

        Spacer(modifier = Modifier.height(NightSpacing.extraLarge))

        CredentialsCard(
            newUsername = uiState.newUsername,
            currentPassword = uiState.currentPassword,
            newPassword = uiState.newPassword,
            confirmNewPassword = uiState.confirmNewPassword,
            errorMessage = uiState.errorMessage,
            isLoading = uiState.isLoading,
            onNewUsernameChange = viewModel::onNewUsernameChange,
            onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
            onNewPasswordChange = viewModel::onNewPasswordChange,
            onConfirmNewPasswordChange = viewModel::onConfirmNewPasswordChange,
            onApplyChanges = {
                viewModel.onApplyChangesClick(
                    currentUsername = userSession.username
                )
            }
        )

        Spacer(modifier = Modifier.height(NightSpacing.extraLarge))

        NightSecondaryButton(
            text = "Cancelar",
            onClick = {
                requestExit()
            },
            enabled = !uiState.isLoading,
            modifier = Modifier.widthIn(
                max = NightSizes.loginCardWidth
            )
        )

        Spacer(modifier = Modifier.height(NightSpacing.extraLarge))
    }

    AccountDialogs(
        showConfirmDialog = uiState.showConfirmDialog,
        showSessionExpiredDialog = uiState.showSessionExpiredDialog,
        showExitConfirmation = showExitConfirmation,
        onConfirmChanges = {
            viewModel.applyConfirmedChanges(
                userId = userSession.userId,
                currentUsername = userSession.username
            )
        },
        onDismissConfirmChanges = {
            viewModel.dismissConfirmDialog()
        },
        onSessionExpiredConfirm = {
            viewModel.clearSessionAndFinish {
                onNavigateToLogin()
            }
        },
        onConfirmExit = {
            showExitConfirmation = false
            onBackToSettings()
        },
        onDismissExit = {
            showExitConfirmation = false
        }
    )
}

@Composable
private fun AccountHeader() {
    Text(
        text = "Actualizar credenciales",
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
private fun CredentialsCard(
    newUsername: String,
    currentPassword: String,
    newPassword: String,
    confirmNewPassword: String,
    errorMessage: String?,
    isLoading: Boolean,
    onNewUsernameChange: (String) -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit,
    onApplyChanges: () -> Unit
) {
    NightBaseCard(
        modifier = Modifier.widthIn(
            max = NightSizes.settingsPanelMaxWidth
        ),
        fillMaxWidth = true,
        contentPadding = PaddingValues(
            horizontal = NightSpacing.extraLarge,
            vertical = NightSpacing.extraLarge
        )
    ) {
        UsernameSection(
            newUsername = newUsername,
            isLoading = isLoading,
            onNewUsernameChange = onNewUsernameChange
        )

        Spacer(modifier = Modifier.height(NightSpacing.extraLarge))

        PasswordSection(
            currentPassword = currentPassword,
            newPassword = newPassword,
            confirmNewPassword = confirmNewPassword,
            isLoading = isLoading,
            onCurrentPasswordChange = onCurrentPasswordChange,
            onNewPasswordChange = onNewPasswordChange,
            onConfirmNewPasswordChange = onConfirmNewPasswordChange
        )

        errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(NightSpacing.medium))

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(NightSpacing.extraLarge))

        NightPrimaryButton(
            text = "Aplicar cambios",
            onClick = onApplyChanges,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        if (isLoading) {
            Spacer(modifier = Modifier.height(NightSpacing.medium))

            CircularProgressIndicator(
                color = CheeseYellow,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun UsernameSection(
    newUsername: String,
    isLoading: Boolean,
    onNewUsernameChange: (String) -> Unit
) {
    SectionTitle(
        title = "Nombre de usuario",
        description = "Deja este campo vacío si no deseas cambiar tu nombre de usuario."
    )

    Spacer(modifier = Modifier.height(NightSpacing.medium))

    NightTextField(
        value = newUsername,
        onValueChange = onNewUsernameChange,
        label = "Nuevo nombre de usuario",
        leadingIcon = Icons.Default.Person,
        singleLine = true,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun PasswordSection(
    currentPassword: String,
    newPassword: String,
    confirmNewPassword: String,
    isLoading: Boolean,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit
) {
    SectionTitle(
        title = "Contraseña",
        description = "Completa los tres campos únicamente si deseas cambiar tu contraseña."
    )

    Spacer(modifier = Modifier.height(NightSpacing.medium))

    NightTextField(
        value = currentPassword,
        onValueChange = onCurrentPasswordChange,
        label = "Contraseña actual",
        leadingIcon = Icons.Default.Lock,
        singleLine = true,
        enabled = !isLoading,
        isPassword = true,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(NightSpacing.medium))

    NightTextField(
        value = newPassword,
        onValueChange = onNewPasswordChange,
        label = "Nueva contraseña",
        leadingIcon = Icons.Default.Lock,
        singleLine = true,
        enabled = !isLoading,
        isPassword = true,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(NightSpacing.medium))

    NightTextField(
        value = confirmNewPassword,
        onValueChange = onConfirmNewPasswordChange,
        label = "Confirmar nueva contraseña",
        leadingIcon = Icons.Default.Lock,
        singleLine = true,
        enabled = !isLoading,
        isPassword = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun SectionTitle(
    title: String,
    description: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(modifier = Modifier.height(NightSpacing.small))

    Text(
        text = description,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun AccountDialogs(
    showConfirmDialog: Boolean,
    showSessionExpiredDialog: Boolean,
    showExitConfirmation: Boolean,
    onConfirmChanges: () -> Unit,
    onDismissConfirmChanges: () -> Unit,
    onSessionExpiredConfirm: () -> Unit,
    onConfirmExit: () -> Unit,
    onDismissExit: () -> Unit
) {
    if (showConfirmDialog) {
        NightMessageDialog(
            title = "Confirmar cambios",
            message = "¿Estás segura de aplicar los cambios en tus credenciales?",
            confirmText = "Sí, aplicar",
            dismissText = "Cancelar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onConfirmChanges,
            onDismiss = onDismissConfirmChanges
        )
    }

    if (showSessionExpiredDialog) {
        NightMessageDialog(
            title = "Credenciales actualizadas",
            message = "Los cambios fueron aplicados correctamente. Debes iniciar sesión nuevamente.",
            confirmText = "Aceptar",
            dismissText = null,
            icon = Icons.Default.CheckCircle,
            iconColor = CheeseYellow,
            onConfirm = onSessionExpiredConfirm,
            onDismiss = null
        )
    }

    if (showExitConfirmation) {
        NightMessageDialog(
            title = "Salir sin guardar",
            message = "Tienes cambios sin aplicar. ¿Deseas salir y descartarlos?",
            confirmText = "Salir",
            dismissText = "Continuar editando",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onConfirmExit,
            onDismiss = onDismissExit
        )
    }
}