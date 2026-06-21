package ni.edu.uam.nightbiteapp.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.components.buttons.NightPrimaryButton
import ni.edu.uam.nightbiteapp.ui.components.fields.NightTextField
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
import ni.edu.uam.nightbiteapp.ui.theme.DarkPurple
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.ui.theme.SoftPurple

@Composable
fun NightRegisterCard(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    usernameError: String?,
    emailError: String?,
    passwordError: String?,
    confirmPasswordError: String?,
    usernameSuccess: Boolean,
    emailSuccess: Boolean,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }

    val anyPasswordVisible = passwordVisible || confirmPasswordVisible

    val isPasswordValid =
        password.isNotBlank() && passwordError == null

    val isConfirmPasswordValid =
        confirmPassword.isNotBlank() &&
                confirmPasswordError == null &&
                password == confirmPassword

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val compact = maxWidth < CompactRegisterBreakpoint

        val layout = if (compact) {
            RegisterCardLayout.compact()
        } else {
            RegisterCardLayout.regular()
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            NightBaseCard(
                modifier = Modifier.fillMaxWidth(),
                fillMaxWidth = true,
                containerColor = SoftPurple,
                borderColor = DarkPurple,
                elevation = 10.dp,
                contentPadding = PaddingValues(
                    start = layout.horizontalPadding,
                    end = layout.horizontalPadding,
                    top = NightSpacing.extraLarge,
                    bottom = NightSpacing.extraLarge
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    RegisterFieldsColumn(
                        username = username,
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        usernameError = usernameError,
                        emailError = emailError,
                        passwordError = passwordError,
                        confirmPasswordError = confirmPasswordError,
                        usernameSuccess = usernameSuccess,
                        emailSuccess = emailSuccess,
                        passwordVisible = passwordVisible,
                        confirmPasswordVisible = confirmPasswordVisible,
                        isPasswordValid = isPasswordValid,
                        isConfirmPasswordValid = isConfirmPasswordValid,
                        fieldWidth = layout.fieldWidth,
                        onUsernameChange = onUsernameChange,
                        onEmailChange = onEmailChange,
                        onPasswordChange = onPasswordChange,
                        onConfirmPasswordChange = onConfirmPasswordChange,
                        onTogglePasswordVisibility = {
                            passwordVisible = !passwordVisible
                        },
                        onToggleConfirmPasswordVisibility = {
                            confirmPasswordVisible = !confirmPasswordVisible
                        }
                    )

                    Spacer(modifier = Modifier.width(layout.leftSpacerWidth))

                    RegisterDivider(
                        height = layout.dividerHeight
                    )

                    Spacer(modifier = Modifier.width(layout.rightSpacerWidth))

                    RegisterActionColumn(
                        anyPasswordVisible = anyPasswordVisible,
                        actionWidth = layout.actionWidth,
                        iconContainerSize = layout.iconContainerSize,
                        iconBackgroundSize = layout.iconBackgroundSize,
                        iconImageSize = layout.iconImageSize,
                        buttonWidth = layout.buttonWidth,
                        onRegisterClick = onRegisterClick
                    )
                }
            }
        }
    }
}

@Composable
private fun RegisterFieldsColumn(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    usernameError: String?,
    emailError: String?,
    passwordError: String?,
    confirmPasswordError: String?,
    usernameSuccess: Boolean,
    emailSuccess: Boolean,
    passwordVisible: Boolean,
    confirmPasswordVisible: Boolean,
    isPasswordValid: Boolean,
    isConfirmPasswordValid: Boolean,
    fieldWidth: Dp,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit
) {
    Column(
        modifier = Modifier.width(fieldWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NightTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = "Usuario",
            icon = Icons.Default.Person,
            isError = usernameError != null,
            isSuccess = usernameSuccess,
            errorMessage = usernameError,
            reserveErrorSpace = true,
            modifier = Modifier.width(fieldWidth)
        )

        NightTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Correo electrónico",
            icon = Icons.Default.Email,
            isError = emailError != null,
            isSuccess = emailSuccess,
            errorMessage = emailError,
            reserveErrorSpace = true,
            modifier = Modifier.width(fieldWidth)
        )

        NightTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Contraseña",
            icon = Icons.Default.Lock,
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = if (passwordVisible) {
                Icons.Default.VisibilityOff
            } else {
                Icons.Default.Visibility
            },
            trailingIconDescription = if (passwordVisible) {
                "Ocultar contraseña"
            } else {
                "Mostrar contraseña"
            },
            onTrailingIconClick = onTogglePasswordVisibility,
            isError = passwordError != null,
            isSuccess = isPasswordValid,
            errorMessage = passwordError,
            reserveErrorSpace = true,
            modifier = Modifier.width(fieldWidth)
        )

        NightTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirmar contraseña",
            icon = Icons.Default.Lock,
            visualTransformation = if (confirmPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = if (confirmPasswordVisible) {
                Icons.Default.VisibilityOff
            } else {
                Icons.Default.Visibility
            },
            trailingIconDescription = if (confirmPasswordVisible) {
                "Ocultar contraseña"
            } else {
                "Mostrar contraseña"
            },
            onTrailingIconClick = onToggleConfirmPasswordVisibility,
            isError = confirmPasswordError != null,
            isSuccess = isConfirmPasswordValid,
            errorMessage = confirmPasswordError,
            reserveErrorSpace = true,
            modifier = Modifier.width(fieldWidth)
        )
    }
}

@Composable
private fun RegisterDivider(
    height: Dp
) {
    Box(
        modifier = Modifier
            .width(RegisterDividerWidth)
            .height(height)
            .background(SmokeWhite.copy(alpha = 0.9f))
    )
}

@Composable
private fun RegisterActionColumn(
    anyPasswordVisible: Boolean,
    actionWidth: Dp,
    iconContainerSize: Dp,
    iconBackgroundSize: Dp,
    iconImageSize: Dp,
    buttonWidth: Dp,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier.width(actionWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RegisterTitleBlock()

        RegisterIconPreview(
            anyPasswordVisible = anyPasswordVisible,
            iconContainerSize = iconContainerSize,
            iconBackgroundSize = iconBackgroundSize,
            iconImageSize = iconImageSize
        )

        Spacer(modifier = Modifier.height(NightSpacing.large))

        NightPrimaryButton(
            text = "REGISTRARSE",
            onClick = onRegisterClick,
            modifier = Modifier.width(buttonWidth)
        )
    }
}

@Composable
private fun RegisterTitleBlock() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CREAR CUENTA",
            color = SmokeWhite,
            fontSize = 25.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.2.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge.copy(
                shadow = Shadow(
                    color = NightSurface,
                    offset = Offset(2f, 2f),
                    blurRadius = 3f
                )
            )
        )

        Spacer(modifier = Modifier.height(NightSpacing.extraSmall))

        Text(
            text = "Regístrate para continuar",
            color = SmokeWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RegisterIconPreview(
    anyPasswordVisible: Boolean,
    iconContainerSize: Dp,
    iconBackgroundSize: Dp,
    iconImageSize: Dp
) {
    Box(
        modifier = Modifier.size(iconContainerSize),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(iconBackgroundSize)
                .background(
                    color = DarkPurple.copy(alpha = 0.55f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(iconContainerSize * 0.20f)
                .align(Alignment.TopEnd)
                .offset(
                    x = 0.dp,
                    y = iconContainerSize * 0.08f
                )
                .background(
                    color = DarkPurple.copy(alpha = 0.7f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(iconContainerSize * 0.11f)
                .align(Alignment.TopEnd)
                .offset(
                    x = iconContainerSize * 0.07f,
                    y = iconContainerSize * 0.26f
                )
                .background(
                    color = DarkPurple.copy(alpha = 0.7f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(iconContainerSize * 0.16f)
                .align(Alignment.BottomStart)
                .offset(
                    x = iconContainerSize * 0.03f,
                    y = -(iconContainerSize * 0.14f)
                )
                .background(
                    color = DarkPurple.copy(alpha = 0.7f),
                    shape = CircleShape
                )
        )

        Image(
            painter = painterResource(
                id = if (anyPasswordVisible) {
                    R.drawable.icono_morado2
                } else {
                    R.drawable.icono_morado1
                }
            ),
            contentDescription = "Icono de registro NightBite",
            modifier = Modifier.size(iconImageSize),
            contentScale = ContentScale.Fit
        )
    }
}

private data class RegisterCardLayout(
    val horizontalPadding: Dp,
    val fieldWidth: Dp,
    val actionWidth: Dp,
    val leftSpacerWidth: Dp,
    val rightSpacerWidth: Dp,
    val dividerHeight: Dp,
    val iconContainerSize: Dp,
    val iconBackgroundSize: Dp,
    val iconImageSize: Dp,
    val buttonWidth: Dp,
    val backButtonOffsetX: Dp,
    val backButtonOffsetY: Dp
) {
    companion object {
        fun compact(): RegisterCardLayout {
            return RegisterCardLayout(
                horizontalPadding = 24.dp,
                fieldWidth = 250.dp,
                actionWidth = 190.dp,
                leftSpacerWidth = 20.dp,
                rightSpacerWidth = 24.dp,
                dividerHeight = 270.dp,
                iconContainerSize = 112.dp,
                iconBackgroundSize = 104.dp,
                iconImageSize = 82.dp,
                buttonWidth = 160.dp,
                backButtonOffsetX = (-42).dp,
                backButtonOffsetY = (-4).dp
            )
        }

        fun regular(): RegisterCardLayout {
            return RegisterCardLayout(
                horizontalPadding = 42.dp,
                fieldWidth = 285.dp,
                actionWidth = 230.dp,
                leftSpacerWidth = 34.dp,
                rightSpacerWidth = 50.dp,
                dividerHeight = 306.dp,
                iconContainerSize = 145.dp,
                iconBackgroundSize = 136.dp,
                iconImageSize = 104.dp,
                buttonWidth = 170.dp,
                backButtonOffsetX = (-54).dp,
                backButtonOffsetY = (-4).dp
            )
        }
    }
}

private val CompactRegisterBreakpoint = 700.dp
private val RegisterDividerWidth = 3.dp