package ni.edu.uam.nightbiteapp.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.ui.components.buttons.NightPrimaryButton
import ni.edu.uam.nightbiteapp.ui.components.cards.NightBaseCard
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.fields.NightTextField
import ni.edu.uam.nightbiteapp.ui.components.layout.NightBackgroundType
import ni.edu.uam.nightbiteapp.ui.components.layout.NightScreenContainer
import ni.edu.uam.nightbiteapp.ui.design.NightDimensions
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.ui.validation.PlayerValidators
import ni.edu.uam.nightbiteapp.viewmodel.PlayerCreationViewModel

@Composable
fun GenderSelectionScreen(
    viewModel: PlayerCreationViewModel,
    onPlayerCreated: () -> Unit,
    onExitApp: () -> Unit = {},
    showWelcomeBackMessage: Boolean = false
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var driverNameTouched by remember {
        mutableStateOf(false)
    }

    var showAllErrors by remember {
        mutableStateOf(false)
    }

    var showRequiredDialog by remember {
        mutableStateOf(false)
    }

    var showApiErrorDialog by remember {
        mutableStateOf(false)
    }

    var showWelcomeDialog by remember(showWelcomeBackMessage) {
        mutableStateOf(showWelcomeBackMessage)
    }

    var lastBackPressTime by remember {
        mutableLongStateOf(0L)
    }

    val driverNameError = if (driverNameTouched || showAllErrors) {
        PlayerValidators.validateDriverName(uiState.driverName)
    } else {
        null
    }

    val genderError = if (showAllErrors) {
        PlayerValidators.validateGender(uiState.gender)
    } else {
        null
    }

    val isDriverNameValid =
        uiState.driverName.isNotBlank() && driverNameError == null

    LaunchedEffect(uiState.isPlayerCreated) {
        if (uiState.isPlayerCreated) {
            Toast.makeText(
                context,
                "¡Bienvenido a NightBite!",
                Toast.LENGTH_SHORT
            ).show()

            onPlayerCreated()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        if (!uiState.errorMessage.isNullOrBlank()) {
            showApiErrorDialog = true
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
                "Retrocede nuevamente para salir",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    NightScreenContainer(
        background = NightBackgroundType.BluePattern,
        useScreenPadding = true,
        scrollable = false,
        avoidKeyboard = true
    ) { dimensions ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            LastStepsCard(
                dimensions = dimensions,
                driverName = uiState.driverName,
                selectedGender = uiState.gender,
                driverNameError = driverNameError,
                genderError = genderError,
                isDriverNameValid = isDriverNameValid,
                isLoading = uiState.isLoading,
                onDriverNameChange = { value ->
                    driverNameTouched = true
                    viewModel.onDriverNameChange(value)
                },
                onGenderSelected = viewModel::onGenderSelected,
                onAccept = {
                    showAllErrors = true

                    val currentNameError =
                        PlayerValidators.validateDriverName(uiState.driverName)

                    val currentGenderError =
                        PlayerValidators.validateGender(uiState.gender)

                    if (currentNameError != null || currentGenderError != null) {
                        showRequiredDialog = true
                    } else {
                        viewModel.createPlayer()
                    }
                }
            )

            if (showRequiredDialog) {
                NightMessageDialog(
                    title = "Datos requeridos",
                    message = "Completa los datos requeridos para finalizar.",
                    confirmText = "ENTENDIDO",
                    icon = Icons.Default.Warning,
                    iconColor = CheeseYellow,
                    onConfirm = {
                        showRequiredDialog = false
                    }
                )
            }

            if (showApiErrorDialog) {
                NightMessageDialog(
                    title = "No se pudo continuar",
                    message = uiState.errorMessage
                        ?: "No se pudo completar la configuración de la cuenta.",
                    confirmText = "ENTENDIDO",
                    icon = Icons.Default.Error,
                    iconColor = PizzaRed,
                    onConfirm = {
                        showApiErrorDialog = false
                        viewModel.clearError()
                    }
                )
            }

            if (showWelcomeDialog) {
                NightMessageDialog(
                    title = "Has vuelto",
                    message = "Completa los últimos pasos para comenzar a jugar.",
                    confirmText = "CONTINUAR",
                    icon = Icons.Default.Warning,
                    iconColor = CheeseYellow,
                    onConfirm = {
                        showWelcomeDialog = false
                    }
                )
            }
        }
    }
}

@Composable
private fun LastStepsCard(
    dimensions: NightDimensions,
    driverName: String,
    selectedGender: String,
    driverNameError: String?,
    genderError: String?,
    isDriverNameValid: Boolean,
    isLoading: Boolean,
    onDriverNameChange: (String) -> Unit,
    onGenderSelected: (String) -> Unit,
    onAccept: () -> Unit
) {
    val cardWidth = dimensions.ageCheckCardWidth * 1.35f
    val cardHeight = dimensions.ageCheckCardHeight * 1.18f
    val contentWidth = cardWidth * 0.78f
    val genderButtonWidth = contentWidth * 0.46f

    NightBaseCard(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .shadow(
                elevation = 8.dp,
                shape = NightShapes.dialog
            ),
        fillMaxWidth = false,
        containerColor = Color(0xFF7894EA),
        contentColor = SmokeWhite,
        borderColor = Color(0xFF4F65C7),
        elevation = 10.dp,
        contentPadding = PaddingValues(
            horizontal = NightSpacing.large,
            vertical = NightSpacing.medium
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LastStepsTitleBlock()

            Column(
                modifier = Modifier.width(contentWidth),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Nombre",
                    color = SmokeWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                NightTextField(
                    value = driverName,
                    onValueChange = onDriverNameChange,
                    label = "Ej: Hazell",
                    icon = Icons.Default.Person,
                    enabled = !isLoading,
                    isError = driverNameError != null,
                    isSuccess = isDriverNameValid,
                    errorMessage = driverNameError,
                    reserveErrorSpace = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Género",
                    color = SmokeWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = NightSpacing.small,
                        alignment = Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GenderButton(
                        text = "Femenino",
                        selected = selectedGender == "Femenino",
                        enabled = !isLoading,
                        width = genderButtonWidth,
                        onClick = {
                            onGenderSelected("Femenino")
                        }
                    )

                    GenderButton(
                        text = "Masculino",
                        selected = selectedGender == "Masculino",
                        enabled = !isLoading,
                        width = genderButtonWidth,
                        onClick = {
                            onGenderSelected("Masculino")
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (genderError != null) {
                        Text(
                            text = genderError,
                            color = PizzaRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            NightPrimaryButton(
                text = if (isLoading) "CREANDO..." else "FINALIZAR",
                onClick = onAccept,
                enabled = !isLoading,
                modifier = Modifier.widthIn(
                    min = contentWidth * 0.62f,
                    max = contentWidth * 0.82f
                )
            )
        }
    }
}

@Composable
private fun LastStepsTitleBlock() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Últimos pasos",
            color = SmokeWhite,
            fontSize = 24.sp,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "Completa tu cuenta",
            color = SmokeWhite.copy(alpha = 0.9f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GenderButton(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    width: Dp,
    onClick: () -> Unit
) {
    val animatedWidth by animateDpAsState(
        targetValue = if (selected) width + 8.dp else width,
        label = "genderButtonWidth"
    )

    val animatedHeight by animateDpAsState(
        targetValue = if (selected) 42.dp else 36.dp,
        label = "genderButtonHeight"
    )

    val animatedColor by animateColorAsState(
        targetValue = if (selected) {
            Color(0xFF35269B)
        } else {
            Color(0xFF5E55C8)
        },
        label = "genderButtonColor"
    )

    val animatedElevation by animateDpAsState(
        targetValue = if (selected) 7.dp else 2.dp,
        label = "genderButtonElevation"
    )

    Box(
        modifier = Modifier
            .width(animatedWidth)
            .height(animatedHeight)
            .shadow(
                elevation = animatedElevation,
                shape = NightShapes.button
            )
            .background(
                color = animatedColor,
                shape = NightShapes.button
            )
            .clickable(enabled = enabled) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = SmokeWhite,
            fontSize = if (selected) 13.sp else 12.sp,
            fontFamily = LilitaOne,
            textAlign = TextAlign.Center
        )
    }
}

private const val EXIT_PRESS_INTERVAL = 2000L