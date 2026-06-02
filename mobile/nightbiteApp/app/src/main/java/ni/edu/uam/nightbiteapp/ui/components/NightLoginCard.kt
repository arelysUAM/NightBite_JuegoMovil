package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
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
import ni.edu.uam.nightbiteapp.ui.theme.NeonCyan
import ni.edu.uam.nightbiteapp.ui.theme.SmokeWhite

/**
 * Tarjeta visual de inicio de sesión.
 *
 * Agrupa los campos del formulario y las acciones principales
 * del acceso a la aplicación.
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
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = NeonCyan
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 22.dp,
                vertical = 18.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = "Inicio de sesión",
                    tint = SmokeWhite,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = "  INICIAR SESIÓN",
                    color = SmokeWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            NightTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = "Usuario o correo",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(8.dp))

            NightTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Contraseña",
                icon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(12.dp))

            NightSecondaryButton(
                text = "CREAR CUENTA",
                onClick = onRegisterClick,
                icon = Icons.Default.PersonAdd
            )

            Spacer(modifier = Modifier.height(10.dp))

            NightPrimaryButton(
                text = "ENTRAR",
                onClick = onLoginClick,
                icon = Icons.Default.Login
            )
        }
    }
}