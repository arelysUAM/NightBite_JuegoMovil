package ni.edu.uam.nightbiteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.AccountCredentialsViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

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
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Actualizar credenciales",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(PaddingValues(16.dp))
            ) {
                Text(
                    text = "Nombre de usuario",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Deja este campo vacío si no deseas cambiar tu nombre de usuario.",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.newUsername,
                    onValueChange = viewModel::onNewUsernameChange,
                    label = {
                        Text(text = "Nuevo nombre de usuario")
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Contraseña",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Completa los tres campos únicamente si deseas cambiar tu contraseña.",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.currentPassword,
                    onValueChange = viewModel::onCurrentPasswordChange,
                    label = {
                        Text(text = "Contraseña actual")
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.newPassword,
                    onValueChange = viewModel::onNewPasswordChange,
                    label = {
                        Text(text = "Nueva contraseña")
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.confirmNewPassword,
                    onValueChange = viewModel::onConfirmNewPasswordChange,
                    label = {
                        Text(text = "Confirmar nueva contraseña")
                    },
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                uiState.errorMessage?.let { errorMessage ->
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.onApplyChangesClick(
                            currentUsername = userSession.username
                        )
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text(text = "Aplicar cambios")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = {
                requestExit()
            },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cancelar")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (uiState.showConfirmDialog) {
        NightMessageDialog(
            title = "Confirmar cambios",
            message = "¿Estás segura de aplicar los cambios en tus credenciales?",
            confirmText = "Sí, aplicar",
            dismissText = "Cancelar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = {
                viewModel.applyConfirmedChanges(
                    userId = userSession.userId,
                    currentUsername = userSession.username
                )
            },
            onDismiss = {
                viewModel.dismissConfirmDialog()
            }
        )
    }

    if (uiState.showSessionExpiredDialog) {
        NightMessageDialog(
            title = "Credenciales actualizadas",
            message = "Los cambios fueron aplicados correctamente. Debes iniciar sesión nuevamente.",
            confirmText = "Aceptar",
            dismissText = null,
            icon = Icons.Default.CheckCircle,
            iconColor = CheeseYellow,
            onConfirm = {
                viewModel.clearSessionAndFinish {
                    onNavigateToLogin()
                }
            },
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
            onConfirm = {
                showExitConfirmation = false
                onBackToSettings()
            },
            onDismiss = {
                showExitConfirmation = false
            }
        )
    }
}