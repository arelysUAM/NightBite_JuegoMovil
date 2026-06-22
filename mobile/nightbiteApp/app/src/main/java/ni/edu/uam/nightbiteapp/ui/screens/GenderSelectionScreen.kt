package ni.edu.uam.nightbiteapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.components.buttons.NightPrimaryButton
import ni.edu.uam.nightbiteapp.ui.components.cards.NightBaseCard
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.fields.NightTextField
import ni.edu.uam.nightbiteapp.ui.components.layout.NightBackgroundType
import ni.edu.uam.nightbiteapp.ui.components.layout.NightScreenContainer
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
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

    var showBackExitHint by remember {
        mutableStateOf(false)
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
            onPlayerCreated()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        if (!uiState.errorMessage.isNullOrBlank()) {
            showApiErrorDialog = true
        }
    }

    LaunchedEffect(showBackExitHint) {
        if (showBackExitHint) {
            delay(1600)
            showBackExitHint = false
        }
    }

    BackHandler {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastBackPressTime < EXIT_PRESS_INTERVAL) {
            onExitApp()
        } else {
            lastBackPressTime = currentTime
            showBackExitHint = true
        }
    }

    NightScreenContainer(
        background = NightBackgroundType.BluePattern,
        useScreenPadding = true,
        scrollable = true,
        avoidKeyboard = true
    ) { _ ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LastStepsCard(
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
                },
                modifier = Modifier.align(Alignment.Center)
            )

            NightInlineMessage(
                visible = showBackExitHint,
                message = "Retrocede nuevamente para salir",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 28.dp)
            )

            if (showRequiredDialog) {
                NightMessageDialog(
                    title = "Datos requeridos",
                    message = "Completa los datos necesarios para finalizar tu registro.",
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
                    message = "Tu ruta nocturna te espera. Completa estos últimos pasos para comenzar a jugar.",
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
    driverName: String,
    selectedGender: String,
    driverNameError: String?,
    genderError: String?,
    isDriverNameValid: Boolean,
    isLoading: Boolean,
    onDriverNameChange: (String) -> Unit,
    onGenderSelected: (String) -> Unit,
    onAccept: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fieldWidth = 285.dp
    val genderButtonWidth = 135.dp
    val errorHeight = 16.dp

    NightBaseCard(
        modifier = modifier.widthIn(
            min = 390.dp,
            max = NightSizes.loginCardWidth
        ),
        fillMaxWidth = false,
        containerColor = Color(0xFF7B92E8),
        contentColor = SmokeWhite,
        borderColor = Color(0xFF556DCE),
        elevation = 10.dp,
        contentPadding = PaddingValues(
            start = NightSpacing.section,
            end = NightSpacing.section,
            top = NightSpacing.extraLarge,
            bottom = NightSpacing.extraLarge
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LastStepsTitle()

            Spacer(modifier = Modifier.height(NightSpacing.extraSmall))

            Text(
                text = "Termina la configuración de tu cuenta",
                color = SmokeWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(NightSpacing.large))

            NightTextField(
                value = driverName,
                onValueChange = onDriverNameChange,
                label = "Nombre",
                icon = Icons.Default.Person,
                enabled = !isLoading,
                isError = driverNameError != null,
                isSuccess = isDriverNameValid,
                errorMessage = null,
                reserveErrorSpace = false,
                modifier = Modifier.width(fieldWidth)
            )

            Spacer(modifier = Modifier.height(2.dp))

            FieldErrorText(
                message = driverNameError,
                width = fieldWidth,
                height = errorHeight
            )

            Spacer(modifier = Modifier.height(NightSpacing.extraSmall))

            Text(
                text = "Seleccione su género",
                color = SmokeWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(fieldWidth),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(NightSpacing.small))

            Row(
                modifier = Modifier.width(fieldWidth),
                horizontalArrangement = Arrangement.spacedBy(
                    space = NightSpacing.medium,
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

            Spacer(modifier = Modifier.height(2.dp))

            FieldErrorText(
                message = genderError,
                width = fieldWidth,
                height = errorHeight
            )

            Spacer(modifier = Modifier.height(NightSpacing.medium))

            NightPrimaryButton(
                text = if (isLoading) "CREANDO..." else "FINALIZAR",
                onClick = onAccept,
                enabled = !isLoading,
                modifier = Modifier
                    .width(160.dp)
                    .height(NightSizes.primaryButtonHeight)
            )
        }
    }
}

@Composable
private fun LastStepsTitle() {
    Text(
        text = "ÚLTIMOS PASOS",
        color = SmokeWhite,
        fontSize = 25.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 2.2.sp,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge.copy(
            shadow = Shadow(
                color = NightSurface,
                offset = Offset(2f, 2f),
                blurRadius = 3f
            )
        )
    )
}

@Composable
private fun FieldErrorText(
    message: String?,
    width: Dp,
    height: Dp,
    startPadding: Dp = 20.dp
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .padding(start = startPadding),
        contentAlignment = Alignment.TopStart
    ) {
        if (message != null) {
            Text(
                text = message,
                color = PizzaRed,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
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
        targetValue = if (selected) width + 6.dp else width,
        label = "genderButtonWidth"
    )

    val animatedColor by animateColorAsState(
        targetValue = if (selected) {
            Color(0xFF3E2EA8)
        } else {
            Color(0xFF6A5ED5)
        },
        label = "genderButtonColor"
    )

    val animatedElevation by animateDpAsState(
        targetValue = if (selected) 7.dp else 3.dp,
        label = "genderButtonElevation"
    )

    Box(
        modifier = Modifier
            .width(animatedWidth)
            .height(NightSizes.primaryButtonHeight)
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
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NightInlineMessage(
    visible: Boolean,
    message: String,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = NightShapes.button
                )
                .background(
                    color = Color(0xEE21143F),
                    shape = NightShapes.button
                )
                .padding(
                    horizontal = NightSpacing.large,
                    vertical = NightSpacing.medium
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_splash),
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(NightSpacing.small))

            Text(
                text = message,
                color = SmokeWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private const val EXIT_PRESS_INTERVAL = 2000L