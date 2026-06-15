package ni.edu.uam.nightbiteapp.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import ni.edu.uam.nightbiteapp.ui.components.buttons.NightPrimaryButton
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
import ni.edu.uam.nightbiteapp.ui.components.cards.NightBaseCard
import ni.edu.uam.nightbiteapp.ui.components.fields.NightTextField
import ni.edu.uam.nightbiteapp.ui.design.NightSizes
import ni.edu.uam.nightbiteapp.ui.design.NightSpacing
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

    NightBaseCard(
        modifier = modifier.widthIn(
            min = 390.dp,
            max = NightSizes.loginCardWidth
        ),
        fillMaxWidth = false,
        containerColor = SoftPurple,
        borderColor = DarkPurple,
        elevation = 10.dp,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
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
            LoginTitle()

            Spacer(modifier = Modifier.height(NightSpacing.small))

            Text(
                text = "Introduce tus credenciales para jugar",
                color = SmokeWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(NightSpacing.large))

            NightTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = "Usuario o correo",
                icon = Icons.Default.Person,
                isError = false,
                errorMessage = null,
                reserveErrorSpace = false,
                modifier = Modifier.width(285.dp)
            )

            Spacer(modifier = Modifier.height(NightSpacing.medium))

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
                modifier = Modifier.width(285.dp)
            )

            Spacer(modifier = Modifier.height(NightSpacing.large))

            NightPrimaryButton(
                text = "INGRESAR",
                onClick = onLoginClick,
                modifier = Modifier
                    .width(160.dp)
                    .height(NightSizes.primaryButtonHeight)
            )

            Spacer(modifier = Modifier.height(NightSpacing.large))

            RegisterLinkRow(
                onRegisterClick = onRegisterClick
            )
        }
    }
}

@Composable
private fun LoginTitle() {
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
}

@Composable
private fun RegisterLinkRow(
    onRegisterClick: () -> Unit
) {
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