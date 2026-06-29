package ni.edu.uam.nightbiteapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.data.local.session.UserSession
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.layout.NightBackgroundType
import ni.edu.uam.nightbiteapp.ui.components.layout.NightScreenContainer
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.design.SettingsDimensions
import ni.edu.uam.nightbiteapp.ui.theme.AccountBorderBlue
import ni.edu.uam.nightbiteapp.ui.theme.AccountButtonPurple
import ni.edu.uam.nightbiteapp.ui.theme.AccountFieldCream
import ni.edu.uam.nightbiteapp.ui.theme.AccountFieldText
import ni.edu.uam.nightbiteapp.ui.theme.AccountHeaderPurple
import ni.edu.uam.nightbiteapp.ui.theme.AccountInnerPurple
import ni.edu.uam.nightbiteapp.ui.theme.AccountPanelBlue
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.viewmodel.AccountCredentialsViewModel
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import ni.edu.uam.nightbiteapp.data.local.preferences.GameplayPreferences

@Composable
fun AccountScreen(
    userSession: UserSession,
    viewModel: AccountCredentialsViewModel,
    onBackToSettings: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userSession.userId) {
        viewModel.loadAccountInfo(userSession.userId)
    }

    fun requestBack() {
        if (!uiState.isLoading) {
            viewModel.onBackAttempt(
                onBackToSettings = onBackToSettings
            )
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
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val availableCardWidth =
                maxWidth - dimensions.iconButtonSize - NightSpacing.extraLarge

            val cardWidth =
                450.dp.coerceAtMost(availableCardWidth)

            val cardHeight =
                350.dp.coerceAtMost(maxHeight - 10.dp)

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

            AccountInfoCard(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(cardWidth)
                    .height(cardHeight),
                username = uiState.username,
                email = uiState.email,
                age = uiState.age,
                gender = uiState.gender,
                createdAt = uiState.createdAt,
                isEditing = uiState.isEditing,
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage,
                usernameError = uiState.usernameError,
                ageError = uiState.ageError,
                genderError = uiState.genderError,
                genderOptions = viewModel.genderOptions,
                onUsernameChange = viewModel::onUsernameChange,
                onAgeChange = viewModel::onAgeChange,
                onGenderSelected = viewModel::onGenderSelected,
                onUpdateClick = viewModel::beginEditing,
                onResetProgressClick = viewModel::onResetProgressClick,
                onCancelClick = viewModel::onCancelEditingClick,
                onSaveClick = viewModel::onSaveChangesClick
            )
        }

        AccountDialogs(
            showSaveConfirmationDialog = uiState.showSaveConfirmationDialog,
            showChangesSavedDialog = uiState.showChangesSavedDialog,
            showCancelConfirmationDialog = uiState.showCancelConfirmationDialog,
            showExitConfirmationDialog = uiState.showExitConfirmationDialog,
            showResetProgressDialog = uiState.showResetProgressDialog,
            showProgressResetSuccessDialog = uiState.showProgressResetSuccessDialog,
            showInvalidDataDialog = uiState.showInvalidDataDialog,
            onConfirmSaveChanges = {
                viewModel.confirmSaveChanges(
                    userId = userSession.userId
                )
            },
            onDismissSaveConfirmation = viewModel::dismissSaveConfirmationDialog,
            onChangesSavedConfirm = viewModel::dismissChangesSavedDialog,
            onConfirmCancelEditing = viewModel::confirmCancelEditing,
            onDismissCancelEditing = viewModel::dismissCancelConfirmationDialog,
            onConfirmExit = {
                viewModel.confirmExitWithoutSaving(
                    onBackToSettings = onBackToSettings
                )
            },
            onDismissExit = viewModel::dismissExitConfirmationDialog,

            onConfirmResetProgress = {
                viewModel.confirmResetProgress(
                    userId = userSession.userId
                )
            },
            onDismissResetProgress = viewModel::dismissResetProgressDialog,
            onProgressResetSuccessConfirm = {
                coroutineScope.launch {
                    GameplayPreferences.resetControlsLayoutToDefault(context)

                    viewModel.finishResetProgressFlow(
                        onNavigateToHome = onNavigateToHome
                    )
                }
            },
            onDismissInvalidData = viewModel::dismissInvalidDataDialog
        )
    }
}

@Composable
private fun AccountInfoCard(
    modifier: Modifier,
    username: String,
    email: String,
    age: String,
    gender: String,
    createdAt: String,
    isEditing: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    usernameError: String?,
    ageError: String?,
    genderError: String?,
    genderOptions: List<String>,
    onUsernameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onGenderSelected: (String) -> Unit,
    onUpdateClick: () -> Unit,
    onResetProgressClick: () -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(NightShapes.panel)
            .background(AccountPanelBlue)
            .border(
                width = 2.dp,
                color = AccountBorderBlue,
                shape = NightShapes.panel
            )
    ) {
        AccountHeader(
            height = 54.dp
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    horizontal = NightSpacing.extraLarge,
                    vertical = NightSpacing.medium
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImmutableAccountPanel(
                email = email,
                createdAt = createdAt,
                height = 76.dp
            )

            Spacer(modifier = Modifier.height(NightSpacing.small))

            EditableAccountPanel(
                username = username,
                age = age,
                gender = gender,
                height = 142.dp,
                isEditing = isEditing,
                isLoading = isLoading,
                usernameError = usernameError,
                ageError = ageError,
                genderError = genderError,
                genderOptions = genderOptions,
                onUsernameChange = onUsernameChange,
                onAgeChange = onAgeChange,
                onGenderSelected = onGenderSelected
            )

            Spacer(modifier = Modifier.height(NightSpacing.medium))

            AccountActionButtons(
                isEditing = isEditing,
                isLoading = isLoading,
                onUpdateClick = onUpdateClick,
                onResetProgressClick = onResetProgressClick,
                onCancelClick = onCancelClick,
                onSaveClick = onSaveClick
            )

            Spacer(modifier = Modifier.height(NightSpacing.medium))

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = PizzaRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun AccountHeader(
    height: Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(AccountHeaderPurple),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Información de la cuenta",
            color = SmokeWhite,
            fontSize = 25.sp,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.8.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge.copy(
                shadow = Shadow(
                    color = NightSurface,
                    offset = Offset(2f, 2f),
                    blurRadius = 3f
                )
            )
        )
    }
}

@Composable
private fun ImmutableAccountPanel(
    email: String,
    createdAt: String,
    height: Dp
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(NightShapes.panel)
            .background(AccountInnerPurple)
            .padding(
                horizontal = NightSpacing.large,
                vertical = NightSpacing.small
            ),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Correo electrónico: $email",
            color = SmokeWhite,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Fecha de registro: $createdAt",
            color = SmokeWhite,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EditableAccountPanel(
    username: String,
    age: String,
    gender: String,
    height: Dp,
    isEditing: Boolean,
    isLoading: Boolean,
    usernameError: String?,
    ageError: String?,
    genderError: String?,
    genderOptions: List<String>,
    onUsernameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onGenderSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(NightShapes.panel)
            .background(AccountInnerPurple)
            .padding(
                horizontal = NightSpacing.large,
                vertical = NightSpacing.medium
            ),
        verticalArrangement = Arrangement.Center
    ) {
        AccountFieldGroup(
            label = "Usuario",
            labelWidth = 60.dp,
            fieldWidth = 155.dp,
            value = username,
            enabled = isEditing && !isLoading,
            errorMessage = usernameError,
            onValueChange = onUsernameChange
        )

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            AccountFieldGroup(
                label = "Edad",
                labelWidth = 60.dp,
                fieldWidth = 72.dp,
                value = age,
                enabled = isEditing && !isLoading,
                errorMessage = ageError,
                keyboardType = KeyboardType.Number,
                onValueChange = onAgeChange
            )

            Spacer(modifier = Modifier.width(28.dp))

            AccountGenderGroup(
                labelWidth = 60.dp,
                fieldWidth = 155.dp,
                selectedGender = gender,
                genderOptions = genderOptions,
                enabled = isEditing && !isLoading,
                errorMessage = genderError,
                onGenderSelected = onGenderSelected
            )
        }
    }
}

@Composable
private fun AccountFieldGroup(
    label: String,
    labelWidth: Dp,
    fieldWidth: Dp,
    value: String,
    enabled: Boolean,
    errorMessage: String?,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AccountFieldLabel(
                text = label,
                width = labelWidth
            )

            AccountSmallTextField(
                value = value,
                enabled = enabled,
                fieldWidth = fieldWidth,
                keyboardType = keyboardType,
                errorMessage = errorMessage,
                onValueChange = onValueChange
            )
        }

        AccountFieldError(
            errorMessage = errorMessage,
            startPadding = labelWidth
        )
    }
}

@Composable
private fun AccountGenderGroup(
    labelWidth: Dp,
    fieldWidth: Dp,
    selectedGender: String,
    genderOptions: List<String>,
    enabled: Boolean,
    errorMessage: String?,
    onGenderSelected: (String) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AccountFieldLabel(
                text = "Género",
                width = labelWidth
            )

            AccountGenderDropdown(
                selectedGender = selectedGender,
                genderOptions = genderOptions,
                enabled = enabled,
                fieldWidth = fieldWidth,
                errorMessage = errorMessage,
                onGenderSelected = onGenderSelected
            )
        }

        AccountFieldError(
            errorMessage = errorMessage,
            startPadding = labelWidth
        )
    }
}

@Composable
private fun AccountFieldLabel(
    text: String,
    width: Dp
) {
    Text(
        text = text,
        color = SmokeWhite,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.width(width)
    )
}

@Composable
private fun AccountSmallTextField(
    value: String,
    enabled: Boolean,
    fieldWidth: Dp,
    keyboardType: KeyboardType,
    errorMessage: String?,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        textStyle = TextStyle(
            color = AccountFieldText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        ),
        modifier = Modifier
            .width(fieldWidth)
            .height(34.dp)
            .clip(NightShapes.smallCard)
            .background(
                if (enabled) {
                    AccountFieldCream
                } else {
                    AccountPanelBlue.copy(alpha = 0.85f)
                }
            )
            .border(
                width = 2.dp,
                color = if (errorMessage != null) {
                    PizzaRed
                } else {
                    Color.Transparent
                },
                shape = NightShapes.smallCard
            )
            .padding(
                horizontal = NightSpacing.medium,
                vertical = 8.dp
            )
    )
}

@Composable
private fun AccountGenderDropdown(
    selectedGender: String,
    genderOptions: List<String>,
    enabled: Boolean,
    fieldWidth: Dp,
    errorMessage: String?,
    onGenderSelected: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Box {
        Row(
            modifier = Modifier
                .width(fieldWidth)
                .height(34.dp)
                .clip(NightShapes.smallCard)
                .background(
                    if (enabled) {
                        AccountFieldCream
                    } else {
                        AccountPanelBlue.copy(alpha = 0.85f)
                    }
                )
                .border(
                    width = 2.dp,
                    color = if (errorMessage != null) {
                        PizzaRed
                    } else {
                        Color.Transparent
                    },
                    shape = NightShapes.smallCard
                )
                .clickable(enabled = enabled) {
                    expanded = true
                }
                .padding(horizontal = NightSpacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedGender.ifBlank { "Seleccionar" },
                color = AccountFieldText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Seleccionar género",
                tint = AccountFieldText,
                modifier = Modifier.size(20.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.background(AccountFieldCream)
        ) {
            genderOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = AccountFieldText,
                            fontSize = 13.sp
                        )
                    },
                    onClick = {
                        onGenderSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun AccountFieldError(
    errorMessage: String?,
    startPadding: Dp
) {
    Box(
        modifier = Modifier
            .height(18.dp)
            .padding(start = startPadding),
        contentAlignment = Alignment.CenterStart
    ) {
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = PizzaRed,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun AccountActionButtons(
    isEditing: Boolean,
    isLoading: Boolean,
    onUpdateClick: () -> Unit,
    onResetProgressClick: () -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val spaceBetweenButtons = NightSpacing.medium

        val leftButtonWidth = if (isEditing) {
            135.dp
        } else {
            150.dp
        }

        val rightButtonMaxWidth = if (isEditing) {
            220.dp
        } else {
            250.dp
        }

        val rightButtonWidth =
            (maxWidth - leftButtonWidth - spaceBetweenButtons)
                .coerceAtMost(rightButtonMaxWidth)

        Row(
            horizontalArrangement = Arrangement.spacedBy(spaceBetweenButtons),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEditing) {
                AccountPillButton(
                    text = "CANCELAR",
                    width = leftButtonWidth,
                    containerColor = PizzaRed,
                    contentColor = SmokeWhite,
                    enabled = !isLoading,
                    onClick = onCancelClick
                )

                AccountPillButton(
                    text = "GUARDAR CAMBIOS",
                    width = rightButtonWidth,
                    containerColor = AccountButtonPurple,
                    contentColor = SmokeWhite,
                    enabled = !isLoading,
                    onClick = onSaveClick
                )
            } else {
                AccountPillButton(
                    text = "ACTUALIZAR",
                    width = leftButtonWidth,
                    containerColor = AccountButtonPurple,
                    contentColor = SmokeWhite,
                    enabled = !isLoading,
                    onClick = onUpdateClick
                )

                AccountPillButton(
                    text = "REINICIAR PROGRESO",
                    width = rightButtonWidth,
                    containerColor = Color.Transparent,
                    contentColor = AccountButtonPurple,
                    borderColor = AccountButtonPurple,
                    enabled = !isLoading,
                    onClick = onResetProgressClick
                )
            }
        }
    }
}

@Composable
private fun AccountPillButton(
    text: String,
    width: Dp,
    containerColor: Color,
    contentColor: Color,
    enabled: Boolean,
    borderColor: Color? = null,
    onClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Box(
        modifier = Modifier
            .width(width)
            .height(SettingsDimensions.actionButtonHeight)
            .clip(NightShapes.button)
            .background(
                if (enabled) {
                    containerColor
                } else {
                    containerColor.copy(alpha = 0.55f)
                }
            )
            .then(
                if (borderColor != null) {
                    Modifier.border(
                        width = SettingsDimensions.actionButtonBorderWidth,
                        color = borderColor,
                        shape = NightShapes.button
                    )
                } else {
                    Modifier
                }
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 13.sp,
            fontFamily = LilitaOne,
            fontWeight = FontWeight.Normal,
            letterSpacing = 1.5.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AccountDialogs(
    showSaveConfirmationDialog: Boolean,
    showChangesSavedDialog: Boolean,
    showCancelConfirmationDialog: Boolean,
    showExitConfirmationDialog: Boolean,
    showResetProgressDialog: Boolean,
    showProgressResetSuccessDialog: Boolean,
    showInvalidDataDialog: Boolean,
    onConfirmSaveChanges: () -> Unit,
    onDismissSaveConfirmation: () -> Unit,
    onChangesSavedConfirm: () -> Unit,
    onConfirmCancelEditing: () -> Unit,
    onDismissCancelEditing: () -> Unit,
    onConfirmExit: () -> Unit,
    onDismissExit: () -> Unit,
    onConfirmResetProgress: () -> Unit,
    onDismissResetProgress: () -> Unit,
    onProgressResetSuccessConfirm: () -> Unit,
    onDismissInvalidData: () -> Unit
) {
    if (showSaveConfirmationDialog) {
        NightMessageDialog(
            title = "Guardar cambios",
            message = "¿Deseas guardar los cambios realizados en tu cuenta?",
            confirmText = "Guardar",
            dismissText = "Cancelar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onConfirmSaveChanges,
            onDismiss = onDismissSaveConfirmation
        )
    }

    if (showChangesSavedDialog) {
        NightMessageDialog(
            title = "Información actualizada",
            message = "Los datos de tu cuenta se actualizaron correctamente.",
            confirmText = "Aceptar",
            icon = Icons.Default.CheckCircle,
            iconColor = CheeseYellow,
            onConfirm = onChangesSavedConfirm
        )
    }

    if (showInvalidDataDialog) {
        NightMessageDialog(
            title = "Datos inválidos",
            message = "No es posible guardar el usuario con estos datos. Intenta de nuevo.",
            confirmText = "Aceptar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onDismissInvalidData
        )
    }

    if (showCancelConfirmationDialog) {
        NightMessageDialog(
            title = "Cancelar cambios",
            message = "Los cambios no se guardarán. ¿Deseas descartarlos?",
            confirmText = "Descartar",
            dismissText = "Continuar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onConfirmCancelEditing,
            onDismiss = onDismissCancelEditing
        )
    }

    if (showExitConfirmationDialog) {
        NightMessageDialog(
            title = "Volver sin guardar",
            message = "Tienes cambios sin guardar. ¿Deseas volver a configuraciones?",
            confirmText = "Volver",
            dismissText = "Continuar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onConfirmExit,
            onDismiss = onDismissExit
        )
    }

    if (showResetProgressDialog) {
        NightMessageDialog(
            title = "Reiniciar progreso",
            message = "Esta acción es irreversible. Se borrarán estrellas, niveles desbloqueados e insignias. ¿Deseas continuar?",
            confirmText = "Reiniciar",
            dismissText = "Cancelar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onConfirmResetProgress,
            onDismiss = onDismissResetProgress
        )
    }

    if (showProgressResetSuccessDialog) {
        NightMessageDialog(
            title = "Progreso reiniciado",
            message = "Tu progreso fue reiniciado correctamente. También se restauraron los controles predeterminados. Volverás al inicio del juego.",
            confirmText = "Aceptar",
            dismissText = null,
            icon = Icons.Default.CheckCircle,
            iconColor = CheeseYellow,
            onConfirm = onProgressResetSuccessConfirm,
            onDismiss = null
        )
    }
}