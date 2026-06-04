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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.viewmodel.AccountCredentialsViewModel

@Composable
fun AccountScreen(
    userSession: UserSession,
    viewModel: AccountCredentialsViewModel,
    onBackToSettings: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Cuenta",
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
                    text = "Datos de cuenta",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Usuario actual: ${userSession.username}")
                Text(text = "Correo: ${userSession.email}")
                Text(text = "Edad: ${userSession.age ?: "No registrada"}")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(PaddingValues(16.dp))
            ) {
                Text(
                    text = "Actualizar credenciales",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.newUsername,
                    onValueChange = viewModel::onNewUsernameChange,
                    label = { Text("Nuevo nombre de usuario") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.currentPassword,
                    onValueChange = viewModel::onCurrentPasswordChange,
                    label = { Text("Contraseña actual") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.newPassword,
                    onValueChange = viewModel::onNewPasswordChange,
                    label = { Text("Nueva contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.confirmNewPassword,
                    onValueChange = viewModel::onConfirmNewPasswordChange,
                    label = { Text("Confirmar nueva contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = uiState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error
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
            onClick = onBackToSettings,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver a configuración")
        }
    }

    if (uiState.showConfirmDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.dismissConfirmDialog()
            },
            title = {
                Text(text = "Confirmar cambios")
            },
            text = {
                Text(text = "¿Estás segura de aplicar los cambios en tus credenciales?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.applyConfirmedChanges(
                            userId = userSession.userId,
                            currentUsername = userSession.username
                        )
                    }
                ) {
                    Text(text = "Sí, aplicar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissConfirmDialog()
                    }
                ) {
                    Text(text = "Cancelar")
                }
            }
        )
    }

    if (uiState.showSessionExpiredDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(text = "Credenciales actualizadas")
            },
            text = {
                Text(text = "Debes iniciar sesión nuevamente.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearSessionAndFinish {
                            onNavigateToLogin()
                        }
                    }
                ) {
                    Text(text = "OK")
                }
            }
        )
    }
}