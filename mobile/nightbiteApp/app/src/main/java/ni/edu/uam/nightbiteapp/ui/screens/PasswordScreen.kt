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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import ni.edu.uam.nightbiteapp.ui.theme.PizzaRed
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.ui.validation.AccountValidators
import androidx.compose.ui.focus.onFocusChanged

private val PasswordPanelBlue = Color(0xFF7B92E8)
private val PasswordHeaderPurple = Color(0xFF5F56CA)
private val PasswordButtonPurple = Color(0xFF3E2EA8)
private val PasswordInnerPurple = Color(0xFF21143F)
private val PasswordFieldCream = Color(0xFFF7F1DE)
private val PasswordFieldText = Color(0xFF21143F)
private val PasswordIconGray = Color(0xFFB7B7B7)
private val PasswordBorderBlue = Color(0xFF556DCE)

@Composable
fun PasswordScreen(
    onBackToSettings: () -> Unit,
    onApplyPasswordChange: (
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ) -> Unit = { _, _, _ -> }
) {
    var currentPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmNewPassword by rememberSaveable { mutableStateOf("") }

    var currentPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var newPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var currentPasswordError by rememberSaveable { mutableStateOf<String?>(null) }
    var newPasswordError by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmPasswordError by rememberSaveable { mutableStateOf<String?>(null) }

    var showInvalidDataDialog by rememberSaveable { mutableStateOf(false) }
    var showCancelDialog by rememberSaveable { mutableStateOf(false) }
    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }

    fun hasChanges(): Boolean {
        return currentPassword.isNotBlank() ||
                newPassword.isNotBlank() ||
                confirmNewPassword.isNotBlank()
    }

    fun validateFields(): Boolean {
        val currentError = AccountValidators.validatePassword(
            password = currentPassword,
            fieldName = "La contraseña actual"
        )

        val newError = AccountValidators.validatePassword(
            password = newPassword,
            fieldName = "La nueva contraseña"
        )

        val confirmError = AccountValidators.validateNewPasswordConfirmation(
            newPassword = newPassword,
            confirmNewPassword = confirmNewPassword
        )

        val repeatedPasswordError = if (
            currentError == null &&
            newError == null &&
            currentPassword == newPassword
        ) {
            "La nueva contraseña debe ser diferente."
        } else {
            null
        }

        currentPasswordError = currentError
        newPasswordError = repeatedPasswordError ?: newError
        confirmPasswordError = confirmError

        return currentPasswordError == null &&
                newPasswordError == null &&
                confirmPasswordError == null
    }

    fun requestBack() {
        if (hasChanges()) {
            showCancelDialog = true
        } else {
            onBackToSettings()
        }
    }

    fun clearFieldsAndBack() {
        currentPassword = ""
        newPassword = ""
        confirmNewPassword = ""

        currentPasswordError = null
        newPasswordError = null
        confirmPasswordError = null

        showCancelDialog = false
        onBackToSettings()
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
                currentPassword = currentPassword,
                newPassword = newPassword,
                confirmNewPassword = confirmNewPassword,
                currentPasswordVisible = currentPasswordVisible,
                newPasswordVisible = newPasswordVisible,
                confirmPasswordVisible = confirmPasswordVisible,
                currentPasswordError = currentPasswordError,
                newPasswordError = newPasswordError,
                confirmPasswordError = confirmPasswordError,
                onCurrentPasswordChange = {
                    currentPassword = it
                    currentPasswordError = null
                },
                onNewPasswordChange = {
                    newPassword = it
                    newPasswordError = null
                },
                onConfirmNewPasswordChange = {
                    confirmNewPassword = it
                    confirmPasswordError = null
                },
                onToggleCurrentPassword = {
                    currentPasswordVisible = !currentPasswordVisible
                },
                onToggleNewPassword = {
                    newPasswordVisible = !newPasswordVisible
                },
                onToggleConfirmPassword = {
                    confirmPasswordVisible = !confirmPasswordVisible
                },
                onCancelClick = {
                    if (hasChanges()) {
                        showCancelDialog = true
                    } else {
                        onBackToSettings()
                    }
                },
                onApplyClick = {
                    val isValid = validateFields()

                    if (!isValid) {
                        showInvalidDataDialog = true
                        return@PasswordCard
                    }

                    onApplyPasswordChange(
                        currentPassword,
                        newPassword,
                        confirmNewPassword
                    )

                    showSuccessDialog = true
                }
            )
        }

        PasswordDialogs(
            showInvalidDataDialog = showInvalidDataDialog,
            showCancelDialog = showCancelDialog,
            showSuccessDialog = showSuccessDialog,
            onDismissInvalidData = {
                showInvalidDataDialog = false
            },
            onDismissCancel = {
                showCancelDialog = false
            },
            onConfirmCancel = {
                clearFieldsAndBack()
            },
            onSuccessConfirm = {
                showSuccessDialog = false
                onBackToSettings()
            }
        )
    }
}

@Composable
private fun PasswordCard(
    modifier: Modifier,
    currentPassword: String,
    newPassword: String,
    confirmNewPassword: String,
    currentPasswordVisible: Boolean,
    newPasswordVisible: Boolean,
    confirmPasswordVisible: Boolean,
    currentPasswordError: String?,
    newPasswordError: String?,
    confirmPasswordError: String?,
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
                currentPassword = currentPassword,
                newPassword = newPassword,
                confirmNewPassword = confirmNewPassword,
                currentPasswordVisible = currentPasswordVisible,
                newPasswordVisible = newPasswordVisible,
                confirmPasswordVisible = confirmPasswordVisible,
                currentPasswordError = currentPasswordError,
                newPasswordError = newPasswordError,
                confirmPasswordError = confirmPasswordError,
                onCurrentPasswordChange = onCurrentPasswordChange,
                onNewPasswordChange = onNewPasswordChange,
                onConfirmNewPasswordChange = onConfirmNewPasswordChange,
                onToggleCurrentPassword = onToggleCurrentPassword,
                onToggleNewPassword = onToggleNewPassword,
                onToggleConfirmPassword = onToggleConfirmPassword
            )

            Spacer(modifier = Modifier.height(NightSpacing.medium))

            PasswordActionButtons(
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
    currentPassword: String,
    newPassword: String,
    confirmNewPassword: String,
    currentPasswordVisible: Boolean,
    newPasswordVisible: Boolean,
    confirmPasswordVisible: Boolean,
    currentPasswordError: String?,
    newPasswordError: String?,
    confirmPasswordError: String?,
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
            value = currentPassword,
            placeholder = "Contraseña actual",
            visible = currentPasswordVisible,
            errorMessage = currentPasswordError,
            onValueChange = onCurrentPasswordChange,
            onToggleVisibility = onToggleCurrentPassword
        )

        Spacer(modifier = Modifier.height(2.dp))

        PasswordField(
            value = newPassword,
            placeholder = "Nueva contraseña",
            visible = newPasswordVisible,
            errorMessage = newPasswordError,
            onValueChange = onNewPasswordChange,
            onToggleVisibility = onToggleNewPassword
        )

        Spacer(modifier = Modifier.height(2.dp))

        PasswordField(
            value = confirmNewPassword,
            placeholder = "Confirmar nueva contraseña",
            visible = confirmPasswordVisible,
            errorMessage = confirmPasswordError,
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

    Column {
        Row(
            modifier = Modifier
                .width(350.dp)
                .height(34.dp)
                .clip(NightShapes.smallCard)
                .background(PasswordFieldCream)
                .border(
                    width = 2.dp,
                    color = if (errorMessage != null) {
                        PizzaRed
                    } else {
                        Color.Transparent
                    },
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
                .padding(start = 42.dp),
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
    onCancelClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PasswordPillButton(
            text = "APLICAR CAMBIOS",
            width = 220.dp,
            containerColor = PasswordButtonPurple,
            contentColor = SmokeWhite,
            onClick = onApplyClick
        )

        PasswordPillButton(
            text = "CANCELAR",
            width = 135.dp,
            containerColor = PizzaRed,
            contentColor = SmokeWhite,
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
            .background(containerColor)
            .clickable(
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
    showInvalidDataDialog: Boolean,
    showCancelDialog: Boolean,
    showSuccessDialog: Boolean,
    onDismissInvalidData: () -> Unit,
    onDismissCancel: () -> Unit,
    onConfirmCancel: () -> Unit,
    onSuccessConfirm: () -> Unit
) {
    if (showInvalidDataDialog) {
        NightMessageDialog(
            title = "Datos inválidos",
            message = "Revisa los campos marcados en rojo antes de aplicar los cambios.",
            confirmText = "Aceptar",
            icon = Icons.Default.Warning,
            iconColor = CheeseYellow,
            onConfirm = onDismissInvalidData
        )
    }

    if (showCancelDialog) {
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

    if (showSuccessDialog) {
        NightMessageDialog(
            title = "Interfaz lista",
            message = "La pantalla ya validó los datos. La conexión con el backend se puede agregar en el siguiente paso.",
            confirmText = "Aceptar",
            icon = Icons.Default.CheckCircle,
            iconColor = CheeseYellow,
            onConfirm = onSuccessConfirm
        )
    }
}