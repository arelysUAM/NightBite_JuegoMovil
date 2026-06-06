package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.ui.theme.CheeseYellow
import ni.edu.uam.nightbiteapp.ui.theme.DarkPurple
import ni.edu.uam.nightbiteapp.ui.theme.LavenderGray
import ni.edu.uam.nightbiteapp.ui.theme.NeonCyan
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite
import ni.edu.uam.nightbiteapp.ui.theme.SoftPurple

@Composable
fun NightLoginCard(
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember {
        mutableStateOf(false)
    }

    val usernameError =
        username.isNotBlank() && username.length < 3

    val passwordError =
        password.isNotBlank() && password.length < 4

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
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
            modifier = Modifier
                .padding(top = 26.dp)
                .widthIn(min = 360.dp, max = 420.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 42.dp,
                        end = 42.dp,
                        top = 60.dp,
                        bottom = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                NightTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = "Usuario o correo",
                    icon = Icons.Default.Person,
                    isError = usernameError,
                    errorMessage = "Debe tener al menos 3 caracteres.",
                    modifier = Modifier.width(270.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

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
                    isError = passwordError,
                    errorMessage = "La contraseña debe tener al menos 4 caracteres.",
                    modifier = Modifier.width(270.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                NightPrimaryButton(
                    text = "INGRESAR",
                    onClick = onLoginClick,
                    modifier = Modifier.width(125.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Crear cuenta",
                    color = SmokeWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable {
                            onRegisterClick()
                        }
                        .padding(vertical = 6.dp)
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(26.dp),
            color = NeonCyan,
            border = BorderStroke(
                width = 5.dp,
                color = NeonCyan.copy(alpha = 0.55f)
            ),
            modifier = Modifier
                .width(188.dp)
                .height(48.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(26.dp)
                )
                .offset(y = 3.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "INICIAR SESIÓN",
                    color = SmokeWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        shadow = Shadow(
                            color = NightSurface,
                            offset = Offset(1.5f, 1.5f),
                            blurRadius = 2f
                        )
                    )
                )
            }
        }
    }
}