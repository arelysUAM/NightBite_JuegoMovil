package ni.edu.uam.nightbiteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    Box(
        modifier = modifier.widthIn(min = 360.dp, max = 460.dp)
    ) {
        Card(
            shape = RoundedCornerShape(36.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF18C7D9)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            modifier = Modifier
                .shadow(10.dp, RoundedCornerShape(36.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(Color(0xFF18C7D9))
                    .height(350.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = "Inicio de sesión",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )

                    Text(
                        text = "  INICIAR SESIÓN",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                NightTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = "Usuario o correo",
                    icon = Icons.Default.Person,
                    modifier = Modifier.widthIn(max = 330.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                NightTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = "Contraseña",
                    icon = Icons.Default.Lock,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.widthIn(max = 330.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                NightSecondaryButton(
                    text = "CREAR CUENTA",
                    onClick = onRegisterClick,
                    icon = Icons.Default.PersonAdd,
                    modifier = Modifier.widthIn(max = 330.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                NightPrimaryButton(
                    text = "ENTRAR",
                    onClick = onLoginClick,
                    icon = Icons.Default.Login,
                    modifier = Modifier.widthIn(max = 260.dp)
                )
            }
        }
    }
}