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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.ui.components.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.ProfileUiState
import ni.edu.uam.nightbiteapp.viewmodel.ProfileViewModel

/**
 * Pantalla de perfil del usuario.
 *
 * Carga el perfil actualizado desde la API usando el id del usuario activo.
 * Muestra datos no sensibles de la cuenta y la ficha Player asignada.
 */
@Composable
fun ProfileScreen(
    userId: Long?,
    onLogout: () -> Unit,
    onNavigateToPlayerCreation: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel()
) {
    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    val uiState = profileViewModel.uiState

    LaunchedEffect(userId) {
        if (userId != null) {
            profileViewModel.loadUserProfile(userId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Perfil",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        when {
            userId == null -> {
                EmptyProfileContent(
                    message = "No hay una sesión activa.",
                    buttonText = "Volver al inicio de sesión",
                    onLogoutClick = {
                        showLogoutDialog = true
                    }
                )
            }

            uiState is ProfileUiState.Loading -> {
                LoadingProfileContent()
            }

            uiState is ProfileUiState.Success -> {
                ProfileContent(
                    user = uiState.user,
                    onLogoutClick = {
                        showLogoutDialog = true
                    },
                    onNavigateToPlayerCreation = onNavigateToPlayerCreation
                )
            }

            uiState is ProfileUiState.Error -> {
                EmptyProfileContent(
                    message = uiState.message,
                    buttonText = "Volver al inicio de sesión",
                    onLogoutClick = {
                        showLogoutDialog = true
                    }
                )
            }

            else -> {
                LoadingProfileContent()
            }
        }
    }

    if (showLogoutDialog) {
        NightMessageDialog(
            title = "Cerrar sesión",
            message = "¿Deseas cerrar sesión y volver al inicio de sesión?",
            confirmText = "Continuar",
            dismissText = "Cancelar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            },
            onDismiss = {
                showLogoutDialog = false
            }
        )
    }
}

@Composable
private fun LoadingProfileContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = CheeseYellow
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Cargando perfil...")
    }
}

@Composable
private fun EmptyProfileContent(
    message: String,
    buttonText: String,
    onLogoutClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp)
        )
    }

    Spacer(modifier = Modifier.height(20.dp))

    Button(
        onClick = onLogoutClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = buttonText)
    }
}

@Composable
private fun ProfileContent(
    user: UserResponse,
    onLogoutClick: () -> Unit,
    onNavigateToPlayerCreation: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(16.dp))
        ) {
            Text(
                text = "Datos de la cuenta",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "ID: ${user.id}")
            Text(text = "Usuario: ${user.username}")
            Text(text = "Correo: ${user.email}")
            Text(text = "Edad: ${user.age}")
            Text(text = "Creado el: ${user.createdAt ?: "No disponible"}")
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
                text = "Ficha del repartidor",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            val player = user.player

            if (player == null) {
                Text(text = "Aún no ha creado su player")

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onNavigateToPlayerCreation,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Crear ficha de repartidor")
                }
            } else {
                Text(text = "ID Player: ${player.id}")
                Text(text = "Apodo: ${player.nickname}")
                Text(text = "Nombre del repartidor: ${player.driverName}")
                Text(text = "Género: ${player.gender}")
                Text(text = "Color de casco: ${player.helmetColor}")
                Text(text = "Tipo de moto: ${player.motorcycleType}")
            }
        }
    }

    Spacer(modifier = Modifier.height(28.dp))

    Button(
        onClick = onLogoutClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Cerrar sesión")
    }
}