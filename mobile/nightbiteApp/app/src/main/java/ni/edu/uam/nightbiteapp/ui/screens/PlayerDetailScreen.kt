package ni.edu.uam.nightbiteapp.ui.screens

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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.data.remote.dto.UserResponse
import ni.edu.uam.nightbiteapp.ui.components.cards.NightBaseCard
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.buttons.NightPrimaryButton
import ni.edu.uam.nightbiteapp.ui.components.buttons.NightSecondaryButton
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.viewmodel.PlayerDetailUiState
import ni.edu.uam.nightbiteapp.viewmodel.PlayerDetailViewModel
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.unit.Dp
import ni.edu.uam.nightbiteapp.ui.design.getNightWindowSize
import ni.edu.uam.nightbiteapp.ui.design.nightDimensionsFor

/**
 * Pantalla que muestra la ficha completa del repartidor.
 *
 * Esta pantalla pertenece al Player, no a la cuenta del usuario.
 * Aquí se muestran los datos del personaje/repartidor dentro del juego.
 */
@Composable
fun PlayerDetailScreen(
    userId: Long?,
    onBackToHome: () -> Unit,
    onNavigateToPlayerCreation: () -> Unit,
    onEditPlayer: () -> Unit = {},
    playerDetailViewModel: PlayerDetailViewModel = viewModel()
) {
    val uiState = playerDetailViewModel.uiState

    LaunchedEffect(userId) {
        if (userId != null) {
            playerDetailViewModel.loadPlayerDetail(userId)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val windowSize = getNightWindowSize(maxWidth)
        val dimensions = nightDimensionsFor(windowSize)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = dimensions.screenHorizontalPadding,
                    vertical = dimensions.screenVerticalPadding
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayerDetailHeader(
                modifier = Modifier.widthIn(
                    max = dimensions.cardMaxWidth
                )
            )

            Spacer(modifier = Modifier.height(dimensions.sectionSpacing))

            when {
                userId == null -> {
                    EmptyPlayerDetailContent(
                        message = "No hay una sesión activa.",
                        buttonText = "Volver al menú principal",
                        cardMaxWidth = dimensions.cardMaxWidth,
                        sectionSpacing = dimensions.sectionSpacing,
                        onButtonClick = onBackToHome
                    )
                }

                uiState is PlayerDetailUiState.Loading -> {
                    LoadingPlayerDetailContent(
                        sectionSpacing = dimensions.sectionSpacing,
                        itemSpacing = dimensions.itemSpacing
                    )
                }

                uiState is PlayerDetailUiState.Success -> {
                    PlayerDetailContent(
                        user = uiState.user,
                        cardMaxWidth = dimensions.cardMaxWidth,
                        sectionSpacing = dimensions.sectionSpacing,
                        itemSpacing = dimensions.itemSpacing,
                        onBackToHome = onBackToHome,
                        onNavigateToPlayerCreation = onNavigateToPlayerCreation,
                        onEditPlayer = onEditPlayer
                    )
                }

                uiState is PlayerDetailUiState.Error -> {
                    EmptyPlayerDetailContent(
                        message = uiState.message,
                        buttonText = "Volver al menú principal",
                        cardMaxWidth = dimensions.cardMaxWidth,
                        sectionSpacing = dimensions.sectionSpacing,
                        onButtonClick = onBackToHome
                    )
                }

                else -> {
                    LoadingPlayerDetailContent(
                        sectionSpacing = dimensions.sectionSpacing,
                        itemSpacing = dimensions.itemSpacing
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensions.sectionSpacing))
        }
    }
}

@Composable
private fun PlayerDetailHeader(
    modifier: Modifier = Modifier
) {
    Text(
        text = "Ficha del repartidor",
        style = MaterialTheme.typography.headlineMedium
    )

    Spacer(modifier = Modifier.height(NightSpacing.small))

    Text(
        text = "Consulta los datos de tu contratación nocturna.",
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

@Composable
private fun LoadingPlayerDetailContent(
    sectionSpacing: Dp,
    itemSpacing: Dp
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = sectionSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = CheeseYellow
        )

        Spacer(modifier = Modifier.height(itemSpacing))

        Text(text = "Cargando ficha del repartidor...")
    }
}

@Composable
private fun EmptyPlayerDetailContent(
    message: String,
    buttonText: String,
    cardMaxWidth: Dp,
    sectionSpacing: Dp,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.widthIn(
            max = cardMaxWidth
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NightBaseCard {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(sectionSpacing))

        NightPrimaryButton(
            text = buttonText,
            onClick = onButtonClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PlayerDetailContent(
    user: UserResponse,
    cardMaxWidth: Dp,
    sectionSpacing: Dp,
    itemSpacing: Dp,
    onBackToHome: () -> Unit,
    onNavigateToPlayerCreation: () -> Unit,
    onEditPlayer: () -> Unit
) {
    val player = user.player

    if (player == null) {
        NightMessageDialog(
            title = "Ficha no disponible",
            message = "Todavía no has creado tu ficha de repartidor.",
            confirmText = "Crear ficha",
            dismissText = "Volver",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onNavigateToPlayerCreation,
            onDismiss = onBackToHome
        )

        return
    }

    Column(
        modifier = Modifier.widthIn(
            max = cardMaxWidth
        )
    ){
        ContractCard(
            playerId = player.id,
            nickname = player.nickname
        )

        Spacer(modifier = Modifier.height(sectionSpacing))

        DriverDataCard(
            driverName = player.driverName,
            gender = player.gender,
            helmetColor = player.helmetColor,
            motorcycleType = player.motorcycleType
        )

        Spacer(modifier = Modifier.height(sectionSpacing))

        WorkDataCard()

        Spacer(modifier = Modifier.height(sectionSpacing))

        NightPrimaryButton(
            text = "Editar ficha",
            onClick = onEditPlayer,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(itemSpacing))

        NightSecondaryButton(
            text = "Volver al menú principal",
            onClick = onBackToHome,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ContractCard(
    playerId: Long,
    nickname: String
) {
    NightBaseCard(
        contentPadding = PaddingValues(
            horizontal = NightSpacing.extraLarge,
            vertical = NightSpacing.extraLarge
        )
    ) {
        Text(
            text = "Contrato NightBite",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(NightSpacing.medium))

        DetailText(label = "ID de jugador", value = "#$playerId")
        DetailText(label = "Apodo", value = nickname)
        DetailText(label = "Noche máxima alcanzada", value = "Nivel 0")
    }
}

@Composable
private fun DriverDataCard(
    driverName: String,
    gender: String,
    helmetColor: String,
    motorcycleType: String
) {
    NightBaseCard(
        contentPadding = PaddingValues(
            horizontal = NightSpacing.extraLarge,
            vertical = NightSpacing.extraLarge
        )
    ) {
        Text(
            text = "Datos del repartidor",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(NightSpacing.medium))

        DetailText(label = "Nombre del repartidor", value = driverName)
        DetailText(label = "Género", value = gender)
        DetailText(label = "Color de casco", value = helmetColor)
        DetailText(label = "Tipo de moto", value = motorcycleType)
    }
}

@Composable
private fun WorkDataCard() {
    NightBaseCard(
        contentPadding = PaddingValues(
            horizontal = NightSpacing.extraLarge,
            vertical = NightSpacing.extraLarge
        )
    ) {
        Text(
            text = "Datos laborales",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(NightSpacing.medium))

        DetailText(label = "Puesto", value = "Repartidor")
        DetailText(label = "Jornada laboral", value = "12:00 a.m. - 3:00 a.m.")
        DetailText(label = "Restaurante", value = "NightBite Pizza")
        DetailText(label = "Estado", value = "Contratado")
    }
}

@Composable
private fun DetailText(
    label: String,
    value: String
) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodyMedium
    )

    Spacer(modifier = Modifier.height(NightSpacing.extraSmall))
}