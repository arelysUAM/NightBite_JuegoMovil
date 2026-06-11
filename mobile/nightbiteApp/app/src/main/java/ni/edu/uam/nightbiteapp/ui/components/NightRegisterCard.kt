package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.R
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
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

    Box(
        modifier = modifier.widthIn(
            min = NightSizes.registerContainerMinWidth,
            max = NightSizes.registerContainerMaxWidth
        ),
        contentAlignment = Alignment.Center
    ) {
        NightBaseCard(
            modifier = Modifier.width(NightSizes.registerCardWidth),
            fillMaxWidth = false,
            containerColor = SoftPurple,
            borderColor = DarkPurple,
            elevation = 10.dp,
            contentPadding = PaddingValues(
                start = 42.dp,
                end = 42.dp,
                top = NightSpacing.extraLarge,
                bottom = NightSpacing.extraLarge
            )
        ) {
            Row(
                modifier = Modifier.widthIn(
                    min = NightSizes.registerContainerMinWidth,
                    max = NightSizes.registerContainerMaxWidth
                ),
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
                    passwordVisible = passwordVisible,
                    confirmPasswordVisible = confirmPasswordVisible,
                    isPasswordValid = isPasswordValid,
                    isConfirmPasswordValid = isConfirmPasswordValid,
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

                Spacer(modifier = Modifier.width(34.dp))

                RegisterDivider()

                Spacer(modifier = Modifier.width(50.dp))

                RegisterActionColumn(
                    anyPasswordVisible = anyPasswordVisible,
                    onRegisterClick = onRegisterClick
                )
            }
        }

        RegisterBackButton(
            onBackToLoginClick = onBackToLoginClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = (-54).dp, y = (-4).dp)
        )
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
    passwordVisible: Boolean,
    confirmPasswordVisible: Boolean,
    isPasswordValid: Boolean,
    isConfirmPasswordValid: Boolean,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit
) {
    Column(
        modifier = Modifier.width(285.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NightTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = "Nombre",
            icon = Icons.Default.Person,
            isError = usernameError != null,
            errorMessage = usernameError,
            reserveErrorSpace = true,
            modifier = Modifier.width(285.dp)
        )

        NightTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Correo electrónico",
            icon = Icons.Default.Email,
            isError = emailError != null,
            errorMessage = emailError,
            reserveErrorSpace = true,
            modifier = Modifier.width(285.dp)
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
            modifier = Modifier.width(285.dp)
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
            modifier = Modifier.width(285.dp)
        )
    }
}

@Composable
private fun RegisterDivider() {
    Box(
        modifier = Modifier
            .width(3.dp)
            .height(306.dp)
            .background(SmokeWhite.copy(alpha = 0.9f))
    )
}

@Composable
private fun RegisterActionColumn(
    anyPasswordVisible: Boolean,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier.width(230.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RegisterTitleBlock()

        RegisterIconPreview(
            anyPasswordVisible = anyPasswordVisible
        )

        Spacer(modifier = Modifier.height(NightSpacing.large))

        NightPrimaryButton(
            text = "REGISTRARSE",
            onClick = onRegisterClick,
            modifier = Modifier.width(170.dp)
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
    anyPasswordVisible: Boolean
) {
    Box(
        modifier = Modifier.size(145.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(136.dp)
                .background(
                    color = DarkPurple.copy(alpha = 0.55f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.TopEnd)
                .offset(x = 0.dp, y = 12.dp)
                .background(
                    color = DarkPurple.copy(alpha = 0.7f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.TopEnd)
                .offset(x = 10.dp, y = 38.dp)
                .background(
                    color = DarkPurple.copy(alpha = 0.7f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.BottomStart)
                .offset(x = 4.dp, y = (-20).dp)
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
            modifier = Modifier.size(104.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun RegisterBackButton(
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.boton_volver),
        contentDescription = "Volver",
        modifier = modifier
            .size(NightSizes.backButton)
            .clickable {
                onBackToLoginClick()
            },
        contentScale = ContentScale.Fit
    )
}