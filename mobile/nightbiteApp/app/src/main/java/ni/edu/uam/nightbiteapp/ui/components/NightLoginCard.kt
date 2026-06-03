package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.nightbiteapp.ui.theme.LavenderGray
import ni.edu.uam.nightbiteapp.ui.theme.NightSurface
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

/**
 * Tarjeta visual centrada para el inicio de sesión.
 *
 * Recibe los datos del formulario y expone acciones para iniciar sesión
 * o navegar hacia el flujo de creación de cuenta.
 */
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
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = NightSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .background(NightSurface)
                .width(380.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = "Iniciar sesión",
                    tint = SmokeWhite
                )

                Text(
                    text = "  INICIAR SESIÓN",
                    color = SmokeWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            NightTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = "Usuario o correo",
                icon = Icons.Default.Person,
                modifier = Modifier.width(300.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            NightTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Contraseña",
                icon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.width(300.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            NightPrimaryButton(
                text = "ENTRAR",
                onClick = onLoginClick,
                icon = Icons.Default.Login,
                modifier = Modifier.width(300.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Crear cuenta",
                color = LavenderGray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    onRegisterClick()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}