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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
        modifier = modifier.widthIn(
            min = 720.dp,
            max = 820.dp
        )
    ) {
        Box(
            modifier = Modifier
                .widthIn(min = 720.dp, max = 820.dp)
                .padding(
                    start = 34.dp,
                    end = 34.dp,
                    top = 26.dp,
                    bottom = 26.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 34.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.width(300.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NightTextField(
                        value = username,
                        onValueChange = onUsernameChange,
                        label = "Nombre",
                        icon = Icons.Default.Person,
                        isError = usernameError != null,
                        errorMessage = usernameError,
                        reserveErrorSpace = true,
                        fieldHeight = 48.dp,
                        modifier = Modifier.width(300.dp)
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    NightTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        label = "Correo electrónico",
                        icon = Icons.Default.Email,
                        isError = emailError != null,
                        errorMessage = emailError,
                        reserveErrorSpace = true,
                        fieldHeight = 48.dp,
                        modifier = Modifier.width(300.dp)
                    )

                    Spacer(modifier = Modifier.height(5.dp))

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
                        fieldHeight = 48.dp,
                        modifier = Modifier.width(300.dp)
                    )

                    Spacer(modifier = Modifier.height(5.dp))

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
                        fieldHeight = 48.dp,
                        modifier = Modifier.width(300.dp)
                    )
                }

                Spacer(modifier = Modifier.width(38.dp))

                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(282.dp)
                        .background(SmokeWhite.copy(alpha = 0.9f))
                )

                Spacer(modifier = Modifier.width(38.dp))

                Column(
                    modifier = Modifier
                        .width(250.dp)
                        .offset(y = (-14).dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )  {
                    Text(
                        text = "Crear Cuenta",
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

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier.size(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .background(
                                    color = DarkPurple.copy(alpha = 0.55f),
                                    shape = CircleShape
                                )
                        )

                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 2.dp, y = 14.dp)
                                .background(
                                    color = DarkPurple.copy(alpha = 0.7f),
                                    shape = CircleShape
                                )
                        )

                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 14.dp, y = 44.dp)
                                .background(
                                    color = DarkPurple.copy(alpha = 0.7f),
                                    shape = CircleShape
                                )
                        )

                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .align(Alignment.BottomStart)
                                .offset(x = 2.dp, y = (-24).dp)
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
                            modifier = Modifier.size(112.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    NightPrimaryButton(
                        text = "REGISTRARSE",
                        onClick = onRegisterClick,
                        modifier = Modifier
                            .width(180.dp)
                            .height(42.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .clickable {
                        onBackToLoginClick()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Volver",
                    tint = DarkPurple,
                    modifier = Modifier.size(15.dp)
                )

                Text(
                    text = "Volver",
                    color = DarkPurple,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}