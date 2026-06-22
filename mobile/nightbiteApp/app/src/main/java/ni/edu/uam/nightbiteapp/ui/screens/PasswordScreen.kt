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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.components.dialogs.NightMessageDialog
import ni.edu.uam.nightbiteapp.ui.components.layout.NightBackgroundType
import ni.edu.uam.nightbiteapp.ui.components.layout.NightScreenContainer
import ni.edu.uam.nightbiteapp.ui.design.NightShapes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.design.SettingsDimensions
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.LilitaOne
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.PasswordBorderBlue
import ni.edu.uam.nightbiteapp.ui.theme.PasswordButtonPurple
import ni.edu.uam.nightbiteapp.ui.theme.PasswordFieldCream
import ni.edu.uam.nightbiteapp.ui.theme.PasswordFieldText
import ni.edu.uam.nightbiteapp.ui.theme.PasswordHeaderPurple
import ni.edu.uam.nightbiteapp.ui.theme.PasswordIconGray
import ni.edu.uam.nightbiteapp.ui.theme.PasswordInnerPurple
import ni.edu.uam.nightbiteapp.ui.theme.PasswordPanelBlue
import ni.edu.uam.nightbiteapp.ui.theme.PasswordValidGreen
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.viewmodel.PasswordUiState
import ni.edu.uam.nightbiteapp.viewmodel.PasswordViewModel

@Composable
fun PasswordScreen(
    userId: Long?,
    passwordViewModel: PasswordViewModel = viewModel(),
    onBackToSettings: () -> Unit,
    onPasswordUpdated: () -> Unit
) {
    val uiState by passwordViewModel.uiState.collectAsState()

    fun requestBack() {
        val canLeave = passwordViewModel.onBackAttempt()

        if (canLeave) {
            onBackToSettings()
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
                420.dp.coerceAtMost(availableCardWidth)

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

            PasswordCard(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(cardWidth)
                    .height(cardHeight),
                uiState = uiState,
                onCurrentPasswordChange = { value ->
                    passwordViewModel.onCurrentPasswordChange(
                        value = value,
                        userId = userId
                    )
                },
                onNewPasswordChange = { value ->
                    passwordViewModel.onNewPasswordChange(value)
                },
                onConfirmNewPasswordChange = { value ->
                    passwordViewModel.onConfirmNewPasswordChange(value)
                },
                onToggleCurrentPassword = {
                    passwordViewModel.toggleCurrentPasswordVisibility()
                },
                onToggleNewPassword = {
                    passwordViewModel.toggleNewPasswordVisibility()
                },
                onToggleConfirmPassword = {
                    passwordViewModel.toggleConfirmPasswordVisibility()
                },
                onCancelClick = {
                    val canLeave = passwordViewModel.onCancelClick()

                    if (canLeave) {
                        onBackToSettings()
                    }
                },
                onApplyClick = {
                    passwordViewModel.onApplyClick(userId)
                }
            )
        }

        PasswordDialogs(
            uiState = uiState,
            onDismissInvalidData = {
                passwordViewModel.dismissInvalidDataDialog()
            },
            onDismissCancel = {
                passwordViewModel.dismissCancelDialog()
            },
            onConfirmCancel = {
                passwordViewModel.confirmCancel()
                onBackToSettings()
            },
            onDismissSaveConfirmation = {
                passwordViewModel.dismissSaveConfirmationDialog()
            },
            onConfirmSavePassword = {
                passwordViewModel.confirmSavePassword(userId)
            },
            onSuccessConfirm = {
                passwordViewModel.confirmSuccess()
                onPasswordUpdated()
            }
        )
    }
}

@Composable
private fun PasswordCard(
    modifier: Modifier,
    uiState: PasswordUiState,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit,
    onToggleCurrentPassword: () -> Unit,
    onToggleNewPassword: () -> Unit,
    onToggleConfirmPassword: () -> Unit,
    onCancelClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(NightShapes.panel)
            .background(PasswordPanelBlue)
            .border(
                width = 2.dp,
                color = PasswordBorderBlue,
                shape = NightShapes.panel
            )
    ) {
        PasswordHeader(
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
            PasswordInnerPanel(
                uiState = uiState,
                onCurrentPasswordChange = onCurrentPasswordChange,
                onNewPasswordChange = onNewPasswordChange,
                onConfirmNewPasswordChange = onConfirmNewPasswordChange,
                onToggleCurrentPassword = onToggleCurrentPassword,
                onToggleNewPassword = onToggleNewPassword,
                onToggleConfirmPassword = onToggleConfirmPassword
            )

            Spacer(modifier = Modifier.height(NightSpacing.medium))

            PasswordActionButtons(
                isSaving = uiState.isSaving,
                onCancelClick = onCancelClick,
                onApplyClick = onApplyClick
            )

            Spacer(modifier = Modifier.height(NightSpacing.medium))
        }
    }
}

@Composable
private fun PasswordHeader(
    height: Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(PasswordHeaderPurple),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Cambiar contraseña",
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
private fun PasswordInnerPanel(
    uiState: PasswordUiState,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit,
    onToggleCurrentPassword: () -> Unit,
    onToggleNewPassword: () -> Unit,
    onToggleConfirmPassword: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(NightShapes.panel)
            .background(PasswordInnerPurple)
            .padding(
                horizontal = NightSpacing.large,
                vertical = NightSpacing.medium
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PasswordField(
            value = uiState.currentPassword,
            placeholder = "Contraseña actual",
            visible = uiState.currentPasswordVisible,
            errorMessage = uiState.currentPasswordError,
            isValid = uiState.currentPasswordIsValid,
            onValueChange = onCurrentPasswordChange,
            onToggleVisibility = onToggleCurrentPassword
        )

        Spacer(modifier = Modifier.height(2.dp))

        PasswordField(
            value = uiState.newPassword,
            placeholder = "Nueva contraseña",
            visible = uiState.newPasswordVisible,
            errorMessage = uiState.newPasswordError,
            isValid = uiState.newPasswordIsValid,
            onValueChange = onNewPasswordChange,
            onToggleVisibility = onToggleNewPassword
        )

        Spacer(modifier = Modifier.height(2.dp))

        PasswordField(
            value = uiState.confirmNewPassword,
            placeholder = "Confirmar nueva contraseña",
            visible = uiState.confirmPasswordVisible,
            errorMessage = uiState.confirmPasswordError,
            isValid = uiState.confirmPasswordIsValid,
            onValueChange = onConfirmNewPasswordChange,
            onToggleVisibility = onToggleConfirmPassword
        )
    }
}

@Composable
private fun PasswordField(
    value: String,
    placeholder: String,
    visible: Boolean,
    errorMessage: String?,
    isValid: Boolean,
    onValueChange: (String) -> Unit,
    onToggleVisibility: () -> Unit
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    val contentColor = if (isFocused) {
        PasswordFieldText
    } else {
        PasswordIconGray
    }

    val borderColor = when {
        errorMessage != null -> PizzaRed
        isValid -> PasswordValidGreen
        else -> Color.Transparent
    }

    Column {
        Row(
            modifier = Modifier
                .width(350.dp)
                .height(34.dp)
                .clip(NightShapes.smallCard)
                .background(PasswordFieldCream)
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = NightShapes.smallCard
                )
                .padding(horizontal = NightSpacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(21.dp)
            )

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = if (visible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                textStyle = TextStyle(
                    color = contentColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = NightSpacing.small)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                decorationBox = { innerTextField ->
                    if (value.isBlank()) {
                        Text(
                            text = placeholder,
                            color = contentColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    innerTextField()
                }
            )

            Icon(
                imageVector = if (visible) {
                    Icons.Default.VisibilityOff
                } else {
                    Icons.Default.Visibility
                },
                contentDescription = "Mostrar contraseña",
                tint = contentColor,
                modifier = Modifier
                    .size(22.dp)
                    .clickable {
                        onToggleVisibility()
                    }
            )
        }

        Box(
            modifier = Modifier
                .width(350.dp)
                .height(18.dp)
                .padding(start = 14.dp),
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
}

@Composable
private fun PasswordActionButtons(
    isSaving: Boolean,
    onCancelClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PasswordPillButton(
            text = if (isSaving) {
                "APLICANDO..."
            } else {
                "APLICAR CAMBIOS"
            },
            width = 220.dp,
            containerColor = PasswordButtonPurple,
            contentColor = SmokeWhite,
            enabled = !isSaving,
            onClick = onApplyClick
        )

        PasswordPillButton(
            text = "CANCELAR",
            width = 135.dp,
            containerColor = PizzaRed,
            contentColor = SmokeWhite,
            enabled = !isSaving,
            onClick = onCancelClick
        )
    }
}

@Composable
private fun PasswordPillButton(
    text: String,
    width: Dp,
    containerColor: Color,
    contentColor: Color,
    enabled: Boolean,
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
private fun PasswordDialogs(
    uiState: PasswordUiState,
    onDismissInvalidData: () -> Unit,
    onDismissCancel: () -> Unit,
    onConfirmCancel: () -> Unit,
    onDismissSaveConfirmation: () -> Unit,
    onConfirmSavePassword: () -> Unit,
    onSuccessConfirm: () -> Unit
) {

    if (uiState.showInvalidDataDialog) {
        NightMessageDialog(
            title = "Datos inválidos",
            message = "Revisa los campos marcados en rojo antes de aplicar los cambios.",
            confirmText = "Aceptar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onDismissInvalidData
        )
    }

    if (uiState.showCancelDialog) {
        NightMessageDialog(
            title = "Cancelar cambios",
            message = "Los cambios no se guardarán. ¿Deseas descartarlos?",
            confirmText = "Descartar",
            dismissText = "Continuar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onConfirmCancel,
            onDismiss = onDismissCancel
        )
    }

    if (uiState.showSaveConfirmationDialog) {
        NightMessageDialog(
            title = "Iniciar sesión nuevamente",
            message = "Al guardar estos cambios deberás iniciar sesión nuevamente. ¿Deseas continuar?",
            confirmText = "Confirmar",
            dismissText = "Cancelar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onConfirmSavePassword,
            onDismiss = onDismissSaveConfirmation
        )
    }

    if (uiState.showSuccessDialog) {
        NightMessageDialog(
            title = "Contraseña actualizada",
            message = "Tu contraseña fue actualizada correctamente. Inicia sesión nuevamente para continuar.",
            confirmText = "Aceptar",
            icon = Icons.Default.CheckCircle,
            iconColor = CheeseYellow,
            onConfirm = onSuccessConfirm
        )
    }
}