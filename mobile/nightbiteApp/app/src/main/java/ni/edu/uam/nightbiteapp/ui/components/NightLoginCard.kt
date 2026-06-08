package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.ui.theme.DarkPurple
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
            min = 390.dp,
            max = 470.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 34.dp,
                    end = 34.dp,
                    top = 22.dp,
                    bottom = 22.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "INICIAR SESIÓN",
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

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Introduce tus credenciales para jugar",
                color = SmokeWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            NightTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = "Usuario o correo",
                icon = Icons.Default.Person,
                isError = false,
                errorMessage = null,
                reserveErrorSpace = false,
                fieldHeight = 54.dp,
                modifier = Modifier.width(285.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

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
                isError = false,
                errorMessage = null,
                reserveErrorSpace = false,
                fieldHeight = 54.dp,
                modifier = Modifier.width(285.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            NightPrimaryButton(
                text = "INGRESAR",
                onClick = onLoginClick,
                modifier = Modifier
                    .width(160.dp)
                    .height(44.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿No tienes una cuenta? ",
                    color = SmokeWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Regístrate aquí",
                    color = DarkPurple,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        onRegisterClick()
                    }
                )
            }
        }
    }
}