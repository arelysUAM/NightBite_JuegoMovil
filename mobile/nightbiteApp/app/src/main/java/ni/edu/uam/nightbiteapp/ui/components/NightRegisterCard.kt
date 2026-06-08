package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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

    val anyPasswordVisible =
        passwordVisible || confirmPasswordVisible

    Box(
        modifier = modifier.widthIn(
            min = 720.dp,
            max = 820.dp
        ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(34.dp),
            border = BorderStroke(
                width = 7.dp,
                color = DarkPurple
            ),
            colors = CardDefaults.cardColors(
                containerColor = SoftPurple
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier.widthIn(
                min = 720.dp,
                max = 820.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .widthIn(min = 720.dp, max = 820.dp)
                    .padding(
                        start = 42.dp,
                        end = 42.dp,
                        top = 24.dp,
                        bottom = 24.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .width(295.dp)
                        .height(306.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    NightTextField(
                        value = username,
                        onValueChange = onUsernameChange,
                        label = "Nombre",
                        icon = Icons.Default.Person,
                        isError = usernameError != null,
                        errorMessage = usernameError,
                        reserveErrorSpace = true,
                        fieldHeight = 54.dp,
                        modifier = Modifier.width(295.dp)
                    )

                    NightTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        label = "Correo electrónico",
                        icon = Icons.Default.Email,
                        isError = emailError != null,
                        errorMessage = emailError,
                        reserveErrorSpace = true,
                        fieldHeight = 54.dp,
                        modifier = Modifier.width(295.dp)
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
                        onTrailingIconClick = {
                            passwordVisible = !passwordVisible
                        },
                        isError = passwordError != null,
                        errorMessage = passwordError,
                        reserveErrorSpace = true,
                        fieldHeight = 54.dp,
                        modifier = Modifier.width(295.dp)
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
                        onTrailingIconClick = {
                            confirmPasswordVisible = !confirmPasswordVisible
                        },
                        isError = confirmPasswordError != null,
                        errorMessage = confirmPasswordError,
                        reserveErrorSpace = true,
                        fieldHeight = 54.dp,
                        modifier = Modifier.width(295.dp)
                    )
                }

                Spacer(modifier = Modifier.width(34.dp))

                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(306.dp)
                        .background(SmokeWhite.copy(alpha = 0.9f))
                )

                Spacer(modifier = Modifier.width(34.dp))

                Column(
                    modifier = Modifier
                        .width(245.dp)
                        .height(306.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "CREAR CUENTA",
                            color = SmokeWhite,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                shadow = Shadow(
                                    color = NightSurface,
                                    offset = Offset(2f, 2f),
                                    blurRadius = 3f
                                )
                            )
                        )

                        Text(
                            text = "Regístrate para continuar",
                            color = SmokeWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

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

                    NightPrimaryButton(
                        text = "REGISTRARSE",
                        onClick = onRegisterClick,
                        modifier = Modifier
                            .width(170.dp)
                            .height(40.dp)
                    )
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.boton_volver),
            contentDescription = "Volver",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-24).dp, y = 22.dp)
                .size(62.dp)
                .clickable {
                    onBackToLoginClick()
                },
            contentScale = ContentScale.Fit
        )
    }
}